package com.blogspot.jabelarminecraft.blocksmith.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

public class InventoryDeconstructResult implements IInventory
{
    private final ItemStack[] stackResult = new ItemStack[9];

    /**
     * Returns the number of slots in the inventory.
     */
    @Override
	public int getSizeInventory()
    {
        return 9;
    }

    /**
     * Returns the stack in slot i
     */
    @Override
	public ItemStack getStackInSlot(int par1)
    {
        return stackResult[par1];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number
     * (second arg) of items and returns them in a new stack.
     */
    @Override
	public ItemStack decrStackSize(int par1, int par2)
    {
        if(stackResult[par1] != null)
        {
            ItemStack itemstack = stackResult[par1];
            stackResult[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop
     * whatever it returns as an EntityItem - like when you close a workbench
     * GUI.
     */
    @Override
	public ItemStack removeStackFromSlot(int par1)
    {
        if(stackResult[par1] != null)
        {
            ItemStack itemstack = stackResult[par1];
            stackResult[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be
     * crafting or armor sections).
     */
    @Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        stackResult[par1] = par2ItemStack;
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be
     * 64, possibly will be extended. *Isn't this more of a set than a get?*
     */
    @Override
	public int getInventoryStackLimit()
    {
        return 1;
    }

    @Override
	public boolean isUsableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return true;
    }

    @Override
	public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack)
    {
        return true;
    }

    @Override
	public boolean isEmpty()
    {
        for(int i = 0; i < stackResult.length; i++ )
        {
            if(stackResult[i] != null)
                return false;
        }
        return true;
    }

    @Override
    public void markDirty()
    {

    }

    @Override
	public String getName()
    {
        return "DeconstructResult";
    }

    @Override
    public boolean hasCustomName()
    {
        return false;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return null;
    }

    @Override
    public void openInventory(EntityPlayer playerIn)
    {

    }

    @Override
    public void closeInventory(EntityPlayer playerIn)
    {

    }

    @Override
    public int getField(int id)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setField(int id, int value)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public int getFieldCount()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void clear()
    {
        for(int i = 0;i<stackResult.length;i++)
            stackResult[i] = null;
    }
}
