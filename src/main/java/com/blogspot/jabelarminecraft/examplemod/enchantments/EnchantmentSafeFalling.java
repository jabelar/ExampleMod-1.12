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

public class EnchantmentSafeFalling extends Enchantment
{

    public EnchantmentSafeFalling()
    {
        super(Rarity.RARE, EnumEnchantmentType.ARMOR_FEET, new EntityEquipmentSlot[] { EntityEquipmentSlot.FEET });
        setRegistryName(MainMod.MODID, "safe_falling");
        setName("safe_falling");
    }

    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    @Override
    public int getMinEnchantability(int enchantmentLevel)
    {
        return enchantmentLevel * 10;
    }

    /**
     * Returns the maximum value of enchantability nedded on the enchantment level passed.
     */
    @Override
    public int getMaxEnchantability(int enchantmentLevel)
    {
        return this.getMinEnchantability(enchantmentLevel) + 15;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    @Override
    public int getMaxLevel()
    {
        return 2;
    }

    /**
     * Determines if the enchantment passed can be applyied together with this enchantment.
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
     */
    @Override
    public boolean canApply(ItemStack stack)
    {
        return stack.getItem() instanceof ItemArmor && ((ItemArmor)stack.getItem()).armorType == EntityEquipmentSlot.FEET;
    }

    /**
     * Called whenever a mob is damaged with an item that has this enchantment on it.
     */
    @Override
    public void onEntityDamaged(EntityLivingBase user, Entity target, int level)
    {
    }

    /**
     * Whenever an entity that has this enchantment on one of its associated items is damaged this method will be
     * called.
     */
    @Override
    public void onUserHurt(EntityLivingBase user, Entity attacker, int level)
    {
    }

    @Override
    public boolean isTreasureEnchantment()
    {
        return true;
    }
    
    /**
     * Calculates the damage protection of the enchantment based on level and damage source passed.
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
     */
    @Override
    public float calcDamageByCreature(int level, EnumCreatureAttribute creatureType)
    {
        return 0.0F;
    }
}