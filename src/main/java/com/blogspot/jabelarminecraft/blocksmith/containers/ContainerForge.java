/**
 * 
 */
package com.blogspot.jabelarminecraft.blocksmith.containers;

import com.blogspot.jabelarminecraft.blocksmith.recipes.ForgeRecipes;
import com.blogspot.jabelarminecraft.blocksmith.slots.SlotForgeFuel;
import com.blogspot.jabelarminecraft.blocksmith.slots.SlotForgeOutput;
import com.blogspot.jabelarminecraft.blocksmith.tileentities.TileEntityForge;

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
 * @author agilroy
 *
 */
public class ContainerForge extends Container
{
    private final IInventory tileForge;
    private int field_178152_f;
    private int field_178153_g;
    private int field_178154_h;
    private int field_178155_i;

    public ContainerForge(InventoryPlayer parInventoryPlayer, IInventory parForgeInventory)
    {
        tileForge = parForgeInventory;
        addSlotToContainer(new Slot(parForgeInventory, 0, 56, 17));
        addSlotToContainer(new SlotForgeFuel(parForgeInventory, 1, 56, 53));
        addSlotToContainer(new SlotForgeOutput(parInventoryPlayer.player, parForgeInventory, 2, 116, 35));
        int i;

        for (i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                addSlotToContainer(new Slot(parInventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i)
        {
            addSlotToContainer(new Slot(parInventoryPlayer, i, 8 + i * 18, 142));
        }
    }

    /**
     * Add the given Listener to the list of Listeners. Method name is for legacy.
     */
    @Override
	public void addListener(IContainerListener parIContainerListenerListener)
    {
        super.addListener(parIContainerListenerListener);
        parIContainerListenerListener.sendAllWindowProperties(this, tileForge);
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

            if (field_178152_f != tileForge.getField(2))
            {
                icrafting.sendWindowProperty(this, 2, tileForge.getField(2));
            }

            if (field_178154_h != tileForge.getField(0))
            {
                icrafting.sendWindowProperty(this, 0, tileForge.getField(0));
            }

            if (field_178155_i != tileForge.getField(1))
            {
                icrafting.sendWindowProperty(this, 1, tileForge.getField(1));
            }

            if (field_178153_g != tileForge.getField(3))
            {
                icrafting.sendWindowProperty(this, 3, tileForge.getField(3));
            }
        }

        field_178152_f = tileForge.getField(2);
        field_178154_h = tileForge.getField(0);
        field_178155_i = tileForge.getField(1);
        field_178153_g = tileForge.getField(3);
    }

    @Override
	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
        tileForge.setField(id, data);
    }

    @Override
	public boolean canInteractWith(EntityPlayer playerIn)
    {
        return tileForge.isUsableByPlayer(playerIn);
    }

    /**
     * Take a stack from the specified inventory slot.
     */
    @Override
	public ItemStack transferStackInSlot(EntityPlayer parPlayer, int parSlotId)
    {
        ItemStack itemstack = null;
        Slot slot = inventorySlots.get(parSlotId);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (parSlotId == 2)
            {
                if (!mergeItemStack(itemstack1, 3, 39, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (parSlotId != 1 && parSlotId != 0)
            {
                if (ForgeRecipes.instance().getSmeltingResult(itemstack1) != null)
                {
                    if (!mergeItemStack(itemstack1, 0, 1, false))
                    {
                        return null;
                    }
                }
                else if (TileEntityForge.isItemFuel(itemstack1))
                {
                    if (!mergeItemStack(itemstack1, 1, 2, false))
                    {
                        return null;
                    }
                }
                else if (parSlotId >= 3 && parSlotId < 30)
                {
                    if (!mergeItemStack(itemstack1, 30, 39, false))
                    {
                        return null;
                    }
                }
                else if (parSlotId >= 30 && parSlotId < 39 && !mergeItemStack(itemstack1, 3, 30, false))
                {
                    return null;
                }
            }
            else if (!mergeItemStack(itemstack1, 3, 39, false))
            {
                return null;
            }

            if (itemstack1.getCount() == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return null;
            }

            slot.onTake(parPlayer, itemstack1);
        }

        return itemstack;
    }
}