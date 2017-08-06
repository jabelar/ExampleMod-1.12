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

package com.blogspot.jabelarminecraft.examplemod.containers;

import com.blogspot.jabelarminecraft.examplemod.recipes.CompactorRecipes;
import com.blogspot.jabelarminecraft.examplemod.slots.SlotCompactorOutput;
import com.blogspot.jabelarminecraft.examplemod.tileentities.TileEntityCompactor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author jabelar
 *
 */
public class ContainerCompactor extends Container
{
    private final IInventory tileCompactor;
    private final int sizeInventory;
    private int ticksCompactingItemSoFar;
    private int ticksPerItem;
    private int timeCanCompact;

    public ContainerCompactor(InventoryPlayer parInventoryPlayer, IInventory parIInventory)
    {
    	// DEBUG
    	System.out.println("ContainerCompactor constructor()");
    	
        tileCompactor = parIInventory;
        sizeInventory = tileCompactor.getSizeInventory();
        addSlotToContainer(new Slot(tileCompactor, TileEntityCompactor.slotEnum.INPUT_SLOT.ordinal(), 56, 35));
        addSlotToContainer(new SlotCompactorOutput(parInventoryPlayer.player, tileCompactor, TileEntityCompactor.slotEnum.OUTPUT_SLOT.ordinal(), 116, 35));
        
        // add player inventory slots
        // note that the slot numbers are within the player inventory so can be same as the tile entity inventory
        int i;
        for (i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                addSlotToContainer(new Slot(parInventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // add hotbar slots
        for (i = 0; i < 9; ++i)
        {
            addSlotToContainer(new Slot(parInventoryPlayer, i, 8 + i * 18, 142));
        }
    }

    /**
     * Add the given Listener to the list of Listeners. Method name is for legacy.
     */
    @Override
    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tileCompactor);
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    @Override
	public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.listeners.size(); ++i)
        {
            IContainerListener iContainerListener = this.listeners.get(i);

            if (ticksCompactingItemSoFar != tileCompactor.getField(2))
            {
                iContainerListener.sendWindowProperty(this, 2, this.tileCompactor.getField(2));
            }

            if (timeCanCompact != tileCompactor.getField(0))
            {
                iContainerListener.sendWindowProperty(this, 2, this.tileCompactor.getField(0));
            }

            if (ticksPerItem != tileCompactor.getField(3))
            {
                iContainerListener.sendWindowProperty(this, 2, this.tileCompactor.getField(3));
            }
        }

        ticksCompactingItemSoFar = tileCompactor.getField(2); // tick compacting item so far
        timeCanCompact = tileCompactor.getField(0); // time can compact
        ticksPerItem = tileCompactor.getField(3); // ticks per item
    }

    @Override
	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
        tileCompactor.setField(id, data);
    }

    @Override
	public boolean canInteractWith(EntityPlayer playerIn)
    {
        return tileCompactor.isUsableByPlayer(playerIn);
    }

    /**
     * Take a stack from the specified inventory slot.
     */
    @Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int slotIndex)
    {
        ItemStack itemStack1 = null;
        Slot slot = inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemStack2 = slot.getStack();
            itemStack1 = itemStack2.copy();

            if (slotIndex == TileEntityCompactor.slotEnum.OUTPUT_SLOT.ordinal())
            {
                if (!mergeItemStack(itemStack2, sizeInventory, sizeInventory+36, true))
                {
                    return null;
                }

                slot.onSlotChange(itemStack2, itemStack1);
            }
            else if (slotIndex != TileEntityCompactor.slotEnum.INPUT_SLOT.ordinal())
            {
            	// check if there is a compacting recipe for the stack
                if (CompactorRecipes.instance().getCompactingResult(itemStack2) != null)
                {
                    if (!mergeItemStack(itemStack2, 0, 1, false))
                    {
                        return null;
                    }
                }
                else if (slotIndex >= sizeInventory && slotIndex < sizeInventory+27) // player inventory slots
                {
                    if (!mergeItemStack(itemStack2, sizeInventory+27, sizeInventory+36, false))
                    {
                        return null;
                    }
                }
                else if (slotIndex >= sizeInventory+27 && slotIndex < sizeInventory+36 && !mergeItemStack(itemStack2, sizeInventory+1, sizeInventory+28, false)) // hotbar slots
                {
                    return null;
                }
            }
            else if (!mergeItemStack(itemStack2, sizeInventory, sizeInventory+36, false))
            {
                return null;
            }

            if (itemStack2.getCount() == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemStack2.getCount() == itemStack1.getCount())
            {
                return null;
            }

            slot.onTake(playerIn, itemStack2);
        }

        return itemStack1;
    }
}