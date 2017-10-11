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
package com.blogspot.jabelarminecraft.examplemod.slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

// TODO: Auto-generated Javadoc
/**
 * @author jabelar
 *
 */
public class SlotOutput extends Slot
{
    /** The player that is using the GUI where this slot resides. */
    protected final EntityPlayer thePlayer;
    private int numOutput;

    /**
     * Instantiates a new slot output.
     *
     * @param parPlayer
     *            the par player
     * @param parIInventory
     *            the par I inventory
     * @param parSlotIndex
     *            the par slot index
     * @param parXDisplayPosition
     *            the par X display position
     * @param parYDisplayPosition
     *            the par Y display position
     */
    public SlotOutput(EntityPlayer parPlayer, IInventory parIInventory, int parSlotIndex, int parXDisplayPosition, int parYDisplayPosition)
    {
        super(parIInventory, parSlotIndex, parXDisplayPosition, parYDisplayPosition);
        this.thePlayer = parPlayer;
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     *
     * @param stack
     *            the stack
     * @return true, if is item valid
     */
    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return false;
    }

    /**
     * Decrease the size of the stack in slot by the amount of the int arg. Returns the new stack.
     *
     * @param parAmount
     *            the par amount
     * @return the item stack
     */
    @Override
    public ItemStack decrStackSize(int parAmount)
    {
        if (getHasStack())
        {
            setNumOutput(getNumOutput() + Math.min(parAmount, getStack().getCount()));
        }

        return super.decrStackSize(parAmount);
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.minecraft.inventory.Slot#onTake(net.minecraft.entity.player.EntityPlayer, net.minecraft.item.ItemStack)
     */
    @Override
    public ItemStack onTake(EntityPlayer playerIn, ItemStack stack)
    {
        this.onCrafting(stack);
        return super.onTake(playerIn, stack);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood. Typically increases an internal count then calls onCrafting(item).
     *
     * @param parItemStack
     *            the par item stack
     * @param parCount
     *            the par count
     */
    @Override
    protected void onCrafting(ItemStack parItemStack, int parCount)
    {
        setNumOutput(getNumOutput() + parCount);
        onCrafting(parItemStack);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
     *
     * @param parItemStack
     *            the par item stack
     */
    @Override
    protected void onCrafting(ItemStack parItemStack)
    {
        // override this in your custom slot class
        // should do things like update achievements/advancements and create experience orbs
    }

    /**
     * Gets the num output.
     *
     * @return the num output
     */
    protected int getNumOutput()
    {
        return numOutput;
    }

    /**
     * Sets the num output.
     *
     * @param numOutput
     *            the new num output
     */
    protected void setNumOutput(int numOutput)
    {
        this.numOutput = numOutput;
    }
}