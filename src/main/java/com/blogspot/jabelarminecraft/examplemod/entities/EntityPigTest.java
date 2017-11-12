/**
    Copyright (C) 2017 by jabelar

    This file is part of jabelar's Minecraft Forge modding examples; as such,
    you can redistribute it and/or modify it under the terms of the GNU
    General Public License as published by the Free Software Foundation,
    either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    For a copy of the GNU General Public License see <http://www.gnu.org/licenses/>.
*/

package com.blogspot.jabelarminecraft.examplemod.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

// TODO: Auto-generated Javadoc
/**
 * @author jabelar
 *
 */
public class EntityPigTest extends EntityPig implements IEntity
{

    /**
     * Instantiates a new entity pig test.
     *
     * @param worldIn
     *            the world in
     */
    public EntityPigTest(World worldIn)
    {
        super(worldIn);
        setSize(1.0F, 10.0F);
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.minecraft.entity.passive.EntityAnimal#attackEntityFrom(net.minecraft.util.DamageSource, float)
     */
    @Override
    public boolean attackEntityFrom(DamageSource parDamageSource, float parDamageAmount)
    {
        // DEBUG
        System.out.println("Test pig has been attacked");
        if (isEntityInvulnerable(parDamageSource))
        {
            // DEBUG
            System.out.println("Entity is invulnerable");
            return false;
        }
        else
        {
            if (!net.minecraftforge.common.ForgeHooks.onLivingAttack(this, parDamageSource, parDamageAmount))
                return false;
            // DEBUG
            System.out.println("ForgeHooks.onLivingAttack returned true");
            if (world.isRemote)
            {
                // DEBUG
                System.out.println("Don't process attack on client");
                return false;
            }
            else
            {
                setGrowingAge(0);

                if (getHealth() <= 0.0F)
                {
                    // DEBUG
                    System.out.println("Health is already below 0");
                    return false;
                }
                else if (parDamageSource.isFireDamage() && isPotionActive(MobEffects.FIRE_RESISTANCE))
                {
                    // DEBUG
                    System.out.println("Attacked with fire but has resistance");
                    return false;
                }
                else
                {
                    if ((parDamageSource == DamageSource.ANVIL || parDamageSource == DamageSource.FALLING_BLOCK)
                            && getItemStackFromSlot(EntityEquipmentSlot.HEAD) != null)
                    {
                        // DEBUG
                        System.out.println("Attacked by falling block without a helmet");
                        getItemStackFromSlot(EntityEquipmentSlot.HEAD).damageItem((int) (parDamageAmount * 4.0F + rand.nextFloat() * parDamageAmount * 2.0F),
                                this);
                        parDamageAmount *= 0.75F;
                    }

                    limbSwingAmount = 1.5F;
                    boolean flag = true;

                    if (hurtResistantTime > maxHurtResistantTime / 2.0F)
                    {
                        // DEBUG
                        System.out.println("Attacked within hurt resistance time");
                        if (parDamageAmount <= lastDamage)
                        {
                            return false;
                        }

                        damageEntity(parDamageSource, parDamageAmount - lastDamage);
                        lastDamage = parDamageAmount;
                        flag = false;
                    }
                    else
                    {
                        // DEBUG
                        System.out.println("Full attack");
                        lastDamage = parDamageAmount;
                        hurtResistantTime = maxHurtResistantTime;
                        damageEntity(parDamageSource, parDamageAmount);
                        hurtTime = maxHurtTime = 10;
                    }

                    attackedAtYaw = 0.0F;
                    Entity entity = parDamageSource.getImmediateSource();

                    if (entity != null)
                    {
                        if (entity instanceof EntityLivingBase)
                        {
                            setRevengeTarget((EntityLivingBase) entity);
                        }

                        if (entity instanceof EntityPlayer)
                        {
                            // DEBUG
                            System.out.println("Attacked by player");
                            recentlyHit = 100;
                            attackingPlayer = (EntityPlayer) entity;
                        }
                        else if (entity instanceof net.minecraft.entity.passive.EntityTameable)
                        {
                            net.minecraft.entity.passive.EntityTameable entitywolf = (net.minecraft.entity.passive.EntityTameable) entity;

                            if (entitywolf.isTamed())
                            {
                                recentlyHit = 100;
                                attackingPlayer = null;
                            }
                        }
                    }

                    if (flag)
                    {
                        world.setEntityState(this, (byte) 2);

                        if (parDamageSource != DamageSource.DROWN)
                        {
                            // DEBUG
                            System.out.println("Not drowning");
                            markVelocityChanged();
                        }

                        if (entity != null)
                        {
                            // DEBUG
                            System.out.println("Processing knockback");
                            double d1 = entity.posX - posX;
                            double d0;

                            for (d0 = entity.posZ - posZ; d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D)
                            {
                                d1 = (Math.random() - Math.random()) * 0.01D;
                            }

                            attackedAtYaw = (float) (Math.atan2(d0, d1) * 180.0D / Math.PI - rotationYaw);
                            knockBack(entity, parDamageAmount, d1, d0);
                        }
                        else
                        {
                            attackedAtYaw = (int) (Math.random() * 2.0D) * 180;
                        }
                    }

                    SoundEvent s;

                    if (getHealth() <= 0.0F)
                    {
                        // DEBUG
                        System.out.println("Health now below 0");
                        s = getDeathSound();

                        if (flag && s != null)
                        {
                            playSound(s, getSoundVolume(), getSoundPitch());
                        }

                        onDeath(parDamageSource);
                    }
                    else
                    {
                        s = getHurtSound(parDamageSource);

                        if (flag && s != null)
                        {
                            playSound(s, getSoundVolume(), getSoundPitch());
                        }
                    }

                    return true;
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.blogspot.jabelarminecraft.examplemod.entities.IEntity#setupAI()
     */
    @Override
    public void setupAI()
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.blogspot.jabelarminecraft.examplemod.entities.IEntity#clearAITasks()
     */
    @Override
    public void clearAITasks()
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.blogspot.jabelarminecraft.examplemod.entities.IEntity#initSyncDataCompound()
     */
    @Override
    public void initSyncDataCompound()
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.blogspot.jabelarminecraft.examplemod.entities.IEntity#getSyncDataCompound()
     */
    @Override
    public NBTTagCompound getSyncDataCompound()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.blogspot.jabelarminecraft.examplemod.entities.IEntity#setSyncDataCompound(net.minecraft.nbt.NBTTagCompound)
     */
    @Override
    public void setSyncDataCompound(NBTTagCompound parCompound)
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.blogspot.jabelarminecraft.examplemod.entities.IEntity#sendEntitySyncPacket()
     */
    @Override
    public void sendEntitySyncPacket()
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.blogspot.jabelarminecraft.examplemod.entities.IEntity#setScaleFactor(float)
     */
    @Override
    public void setScaleFactor(float parScaleFactor)
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.blogspot.jabelarminecraft.examplemod.entities.IEntity#getScaleFactor()
     */
    @Override
    public float getScaleFactor()
    {
        // TODO Auto-generated method stub
        return 0;
    }
}
