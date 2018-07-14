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
package com.blogspot.jabelarminecraft.examplemod.enchantments;

import com.blogspot.jabelarminecraft.examplemod.MainMod;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

// TODO: Auto-generated Javadoc
public class EnchantmentSafeFalling extends Enchantment
{

    /**
     * Instantiates a new enchantment safe falling.
     */
    public EnchantmentSafeFalling()
    {
        super(Rarity.RARE, EnumEnchantmentType.ARMOR_FEET, new EntityEquipmentSlot[] { EntityEquipmentSlot.FEET });
        setRegistryName(MainMod.MODID, "safe_falling");
        setName("safe_falling");
    }

    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     *
     * @param enchantmentLevel the enchantment level
     * @return the min enchantability
     */
    @Override
    public int getMinEnchantability(int enchantmentLevel)
    {
        return enchantmentLevel * 10;
    }

    /**
     * Returns the maximum value of enchantability nedded on the enchantment level passed.
     *
     * @param enchantmentLevel the enchantment level
     * @return the max enchantability
     */
    @Override
    public int getMaxEnchantability(int enchantmentLevel)
    {
        return this.getMinEnchantability(enchantmentLevel) + 15;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     *
     * @return the max level
     */
    @Override
    public int getMaxLevel()
    {
        return 2;
    }

    /**
     * Determines if the enchantment passed can be applyied together with this enchantment.
     *
     * @param ench the ench
     * @return true, if successful
     */
    @Override
    public boolean canApplyTogether(Enchantment ench)
    {
        return super.canApplyTogether(ench) 
                && ench != Enchantments.DEPTH_STRIDER 
                && ench != Enchantments.FROST_WALKER
                && ench != Enchantments.FEATHER_FALLING;
    }
    

    /**
     * Determines if this enchantment can be applied to a specific ItemStack.
     *
     * @param stack the stack
     * @return true, if successful
     */
    @Override
    public boolean canApply(ItemStack stack)
    {
        return stack.getItem() instanceof ItemArmor && ((ItemArmor)stack.getItem()).armorType == EntityEquipmentSlot.FEET;
    }

    /**
     * Called whenever a mob is damaged with an item that has this enchantment on it.
     *
     * @param user the user
     * @param target the target
     * @param level the level
     */
    @Override
    public void onEntityDamaged(EntityLivingBase user, Entity target, int level)
    {
    }

    /**
     * Whenever an entity that has this enchantment on one of its associated items is damaged this method will be
     * called.
     *
     * @param user the user
     * @param attacker the attacker
     * @param level the level
     */
    @Override
    public void onUserHurt(EntityLivingBase user, Entity attacker, int level)
    {
    }

    /* (non-Javadoc)
     * @see net.minecraft.enchantment.Enchantment#isTreasureEnchantment()
     */
    @Override
    public boolean isTreasureEnchantment()
    {
        return true;
    }
    
    /**
     * Calculates the damage protection of the enchantment based on level and damage source passed.
     *
     * @param level the level
     * @param source the source
     * @return the int
     */
    @Override
    public int calcModifierDamage(int level, DamageSource source)
    {
        if (source != DamageSource.FALL) return 0;
        
        // DEBUG
        System.out.println("EnchantmentSafeFalling has modified the damage");
        
        return 256;
    }

    /**
     * Calculates the additional damage that will be dealt by an item with this enchantment. This alternative to
     * calcModifierDamage is sensitive to the targets EnumCreatureAttribute.
     *
     * @param level the level
     * @param creatureType the creature type
     * @return the float
     */
    @Override
    public float calcDamageByCreature(int level, EnumCreatureAttribute creatureType)
    {
        return 0.0F;
    }
}