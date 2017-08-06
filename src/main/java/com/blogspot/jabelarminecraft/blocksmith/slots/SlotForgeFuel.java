/**
 * 
 */
package com.blogspot.jabelarminecraft.blocksmith.slots;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.blogspot.jabelarminecraft.blocksmith.tileentities.TileEntityForge;

/**
 * @author jabelar
 *
 */
public class SlotForgeFuel extends Slot
{
    public SlotForgeFuel(IInventory p_i45795_1_, int p_i45795_2_, int p_i45795_3_, int p_i45795_4_)
    {
        super(p_i45795_1_, p_i45795_2_, p_i45795_3_, p_i45795_4_);
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    @Override
	public boolean isItemValid(ItemStack stack)
    {
        return TileEntityForge.isItemFuel(stack) || isItemBucket(stack);
    }

    @Override
	public int getItemStackLimit(ItemStack parItemStack)
    {
        return isItemValid(parItemStack) ? 1 : super.getItemStackLimit(parItemStack);
    }

    public static boolean isItemBucket(ItemStack parItemStack)
    {
        return parItemStack != null && parItemStack.getItem() != null && parItemStack.getItem() == Items.BUCKET;
    }
}