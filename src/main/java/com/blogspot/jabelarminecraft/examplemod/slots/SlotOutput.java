/**
 * 
 */
package com.blogspot.jabelarminecraft.examplemod.slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * @author jabelar
 *
 */
public class SlotOutput extends Slot
{
    /** The player that is using the GUI where this slot resides. */
    protected final EntityPlayer thePlayer;
    private int numOutput;

    public SlotOutput(EntityPlayer parPlayer, IInventory parIInventory, int parSlotIndex, int parXDisplayPosition, int parYDisplayPosition)
    {
        super(parIInventory, parSlotIndex, parXDisplayPosition, parYDisplayPosition);
        this.thePlayer = parPlayer;
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    @Override
	public boolean isItemValid(ItemStack stack)
    {
        return false;
    }

    /**
     * Decrease the size of the stack in slot by the amount of the int arg. Returns the new
     * stack.
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

    @Override
	public ItemStack onTake(EntityPlayer playerIn, ItemStack stack)
    {
        this.onCrafting(stack);
        return super.onTake(playerIn, stack);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood. Typically increases an
     * internal count then calls onCrafting(item).
     */
    @Override
	protected void onCrafting(ItemStack parItemStack, int parCount)
    {
        setNumOutput(getNumOutput() + parCount);
        onCrafting(parItemStack);
    }
    
    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
     */
    @Override
	protected void onCrafting(ItemStack parItemStack)
    {
    	// override this in your custom slot class
    	// should do things like update achievements/advancements and create experience orbs
    }

	protected int getNumOutput() 
	{
		return numOutput;
	}

	protected void setNumOutput(int numOutput) 
	{
		this.numOutput = numOutput;
	}
}