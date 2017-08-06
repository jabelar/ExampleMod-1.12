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

package com.blogspot.jabelarminecraft.blocksmith.containers;

import com.blogspot.jabelarminecraft.blocksmith.recipes.TanningRackRecipes;
import com.blogspot.jabelarminecraft.blocksmith.slots.SlotTanningRackOutput;
import com.blogspot.jabelarminecraft.blocksmith.tileentities.TileEntityTanningRack;

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
public class ContainerTanningRack extends Container
{
    private final IInventory tileTanningRack;
    private final int sizeInventory;
    private int ticksTanningItemSoFar;
    private int ticksPerItem;
    private int timeCanGrind;

    public ContainerTanningRack(InventoryPlayer parInventoryPlayer, IInventory parIInventory)
    {
    	// DEBUG
    	System.out.println("ContainerTanningRack constructor()");
    	
        tileTanningRack = parIInventory;
        sizeInventory = tileTanningRack.getSizeInventory();
        addSlotToContainer(new Slot(tileTanningRack, TileEntityTanningRack.slotEnum.INPUT_SLOT.ordinal(), 56, 35));
        addSlotToContainer(new SlotTanningRackOutput(parInventoryPlayer.player, tileTanningRack, TileEntityTanningRack.slotEnum.OUTPUT_SLOT.ordinal(), 116, 35));
        
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
        listener.sendAllWindowProperties(this, tileTanningRack);
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    @Override
	public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < listeners.size(); ++i)
        {
            IContainerListener icrafting = listeners.get(i);

            if (ticksTanningItemSoFar != tileTanningRack.getField(2))
            {
                icrafting.sendWindowProperty(this, 2, tileTanningRack.getField(2));
            }

            if (timeCanGrind != tileTanningRack.getField(0))
            {
                icrafting.sendWindowProperty(this, 0, tileTanningRack.getField(0));
            }

            if (ticksPerItem != tileTanningRack.getField(3))
            {
                icrafting.sendWindowProperty(this, 3, tileTanningRack.getField(3));
            }
        }

        ticksTanningItemSoFar = tileTanningRack.getField(2); // tick tanning item so far
        timeCanGrind = tileTanningRack.getField(0); // time can grind
        ticksPerItem = tileTanningRack.getField(3); // ticks per item
    }

    @Override
	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
        tileTanningRack.setField(id, data);
    }

    @Override
	public boolean canInteractWith(EntityPlayer playerIn)
    {
        return tileTanningRack.isUsableByPlayer(playerIn);
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

            if (slotIndex == TileEntityTanningRack.slotEnum.OUTPUT_SLOT.ordinal())
            {
                if (!mergeItemStack(itemStack2, sizeInventory, sizeInventory+36, true))
                {
                    return null;
                }

                slot.onSlotChange(itemStack2, itemStack1);
            }
            else if (slotIndex != TileEntityTanningRack.slotEnum.INPUT_SLOT.ordinal())
            {
            	// check if there is a tanning recipe for the stack
                if (TanningRackRecipes.instance().getTanningResult(itemStack2) != null)
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
                else if (slotIndex >= sizeInventory+27 && slotIndex < sizeInventory+36 && !mergeItemStack(itemStack2, sizeInventory+1, sizeInventory+27, false)) // hotbar slots
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