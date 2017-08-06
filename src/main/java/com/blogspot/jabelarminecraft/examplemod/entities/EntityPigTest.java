/**
    Copyright (C) 2015 by jabelar

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
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
/**
 * @author jabelar
 *
 */
public class EntityPigTest extends EntityPig
{
	public EntityPigTest(World worldIn) 
	{
		super(worldIn);
		setSize(1.0F, 10.0F);
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource parDamageSource, float parDamageAmount)
	{
		// DEBUG
		System.out.println("Test pig has been attacked");
        if (this.isEntityInvulnerable(parDamageSource))
        {
        	// DEBUG
        	System.out.println("Entity is invulnerable");
            return false;
        }
        else
        {
            if (!net.minecraftforge.common.ForgeHooks.onLivingAttack(this, parDamageSource, parDamageAmount)) return false;
            // DEBUG
            System.out.println("ForgeHooks.onLivingAttack returned true");
            if (this.world.isRemote)
            {
            	// DEBUG
            	System.out.println("Don't process attack on client");
                return false;
            }
            else
            {
                this.setGrowingAge(0);

                if (this.getHealth() <= 0.0F)
                {
                	// DEBUG
                	System.out.println("Health is already below 0");
                    return false;
                }
                else if (parDamageSource.isFireDamage() && this.isPotionActive(MobEffects.FIRE_RESISTANCE))
                {
                	// DEBUG
                	System.out.println("Attacked with fire but has resistance");
                    return false;
                }
                else
                {
                    if ((parDamageSource == DamageSource.ANVIL || parDamageSource == DamageSource.FALLING_BLOCK) && this.getItemStackFromSlot(EntityEquipmentSlot.HEAD) != null)
                    {
                    	// DEBUG
                    	System.out.println("Attacked by falling block without a helmet");
                        this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).damageItem((int)(parDamageAmount * 4.0F + this.rand.nextFloat() * parDamageAmount * 2.0F), this);
                        parDamageAmount *= 0.75F;
                    }

                    this.limbSwingAmount = 1.5F;
                    boolean flag = true;

                    if (this.hurtResistantTime > this.maxHurtResistantTime / 2.0F)
                    {
                    	// DEBUG
                    	System.out.println("Attacked within hurt resistance time");
                        if (parDamageAmount <= this.lastDamage)
                        {
                            return false;
                        }

                        this.damageEntity(parDamageSource, parDamageAmount - this.lastDamage);
                        this.lastDamage = parDamageAmount;
                        flag = false;
                    }
                    else
                    {
                    	// DEBUG
                    	System.out.println("Full attack");
                        this.lastDamage = parDamageAmount;
                        this.hurtResistantTime = this.maxHurtResistantTime;
                        this.damageEntity(parDamageSource, parDamageAmount);
                        this.hurtTime = this.maxHurtTime = 10;
                    }

                    this.attackedAtYaw = 0.0F;
                    Entity entity = parDamageSource.getImmediateSource();

                    if (entity != null)
                    {
                        if (entity instanceof EntityLivingBase)
                        {
                            this.setRevengeTarget((EntityLivingBase)entity);
                        }

                        if (entity instanceof EntityPlayer)
                        {
                        	// DEBUG
                        	System.out.println("Attacked by player");
                            this.recentlyHit = 100;
                            this.attackingPlayer = (EntityPlayer)entity;
                        }
                        else if (entity instanceof net.minecraft.entity.passive.EntityTameable)
                        {
                            net.minecraft.entity.passive.EntityTameable entitywolf = (net.minecraft.entity.passive.EntityTameable)entity;

                            if (entitywolf.isTamed())
                            {
                                this.recentlyHit = 100;
                                this.attackingPlayer = null;
                            }
                        }
                    }

                    if (flag)
                    {
                        this.world.setEntityState(this, (byte)2);

                        if (parDamageSource != DamageSource.DROWN)
                        {
                        	// DEBUG
                        	System.out.println("Not drowning");
                            this.setBeenAttacked();
                        }

                        if (entity != null)
                        {
                        	// DEBUG
                        	System.out.println("Processing knockback");
                            double d1 = entity.posX - this.posX;
                            double d0;

                            for (d0 = entity.posZ - this.posZ; d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D)
                            {
                                d1 = (Math.random() - Math.random()) * 0.01D;
                            }

                            this.attackedAtYaw = (float)(Math.atan2(d0, d1) * 180.0D / Math.PI - this.rotationYaw);
                            this.knockBack(entity, parDamageAmount, d1, d0);
                        }
                        else
                        {
                            this.attackedAtYaw = (int)(Math.random() * 2.0D) * 180;
                        }
                    }

                    SoundEvent s;

                    if (this.getHealth() <= 0.0F)
                    {
                    	// DEBUG
                    	System.out.println("Health now below 0");
                        s = this.getDeathSound();

                        if (flag && s != null)
                        {
                            this.playSound(s, this.getSoundVolume(), this.getSoundPitch());
                        }

                        this.onDeath(parDamageSource);
                    }
                    else
                    {
                        s = this.getHurtSound(parDamageSource);

                        if (flag && s != null)
                        {
                            this.playSound(s, this.getSoundVolume(), this.getSoundPitch());
                        }
                    }

                    return true;
                }
            }
        }
    }
}
