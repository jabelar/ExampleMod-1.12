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

import com.blogspot.jabelarminecraft.blocksmith.recipes.GrinderRecipes;
import com.blogspot.jabelarminecraft.blocksmith.slots.SlotGrinderOutput;
import com.blogspot.jabelarminecraft.blocksmith.tileentities.TileEntityGrinder;

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
public class ContainerGrinder extends Container
{
    private final IInventory tileGrinder;
    private final int sizeInventory;
    private int ticksGrindingItemSoFar;
    private int ticksPerItem;
    private int timeCanGrind;

    public ContainerGrinder(InventoryPlayer parInventoryPlayer, IInventory parIInventory)
    {
        // DEBUG
        System.out.println("ContainerGrinder constructor()");
        
        tileGrinder = parIInventory;
        sizeInventory = tileGrinder.getSizeInventory();
        addSlotToContainer(new Slot(tileGrinder, TileEntityGrinder.slotEnum.INPUT_SLOT.ordinal(), 56, 35));
        addSlotToContainer(new SlotGrinderOutput(parInventoryPlayer.player, tileGrinder, TileEntityGrinder.slotEnum.OUTPUT_SLOT.ordinal(), 116, 35));
        
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
        listener.sendAllWindowProperties(this, tileGrinder);
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

            if (ticksGrindingItemSoFar != tileGrinder.getField(2))
            {
                icrafting.sendWindowProperty(this, 2, tileGrinder.getField(2));
            }

            if (timeCanGrind != tileGrinder.getField(0))
            {
                icrafting.sendWindowProperty(this, 0, tileGrinder.getField(0));
            }

            if (ticksPerItem != tileGrinder.getField(3))
            {
                icrafting.sendWindowProperty(this, 3, tileGrinder.getField(3));
            }
        }

        ticksGrindingItemSoFar = tileGrinder.getField(2); // tick grinding item so far
        timeCanGrind = tileGrinder.getField(0); // time can grind
        ticksPerItem = tileGrinder.getField(3); // ticks per item
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
        tileGrinder.setField(id, data);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return tileGrinder.isUsableByPlayer(playerIn);
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

            if (slotIndex == TileEntityGrinder.slotEnum.OUTPUT_SLOT.ordinal())
            {
                if (!mergeItemStack(itemStack2, sizeInventory, sizeInventory+36, true))
                {
                    return null;
                }

                slot.onSlotChange(itemStack2, itemStack1);
            }
            else if (slotIndex != TileEntityGrinder.slotEnum.INPUT_SLOT.ordinal())
            {
                // check if there is a grinding recipe for the stack
                if (GrinderRecipes.instance().getGrindingResult(itemStack2) != null)
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