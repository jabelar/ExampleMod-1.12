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

package com.blogspot.jabelarminecraft.blocksmith.tileentities;

import com.blogspot.jabelarminecraft.blocksmith.blocks.BlockGrinder;
import com.blogspot.jabelarminecraft.blocksmith.containers.ContainerGrinder;
import com.blogspot.jabelarminecraft.blocksmith.recipes.GrinderRecipes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author jabelar
 *
 */
public class TileEntityGrinder extends TileEntityLockable implements ITickable, ISidedInventory
{
    // enumerate the slots
    public enum slotEnum 
    {
        INPUT_SLOT, OUTPUT_SLOT
    }
    private static final int[] slotsTop = new int[] {slotEnum.INPUT_SLOT.ordinal()};
    private static final int[] slotsBottom = new int[] {slotEnum.OUTPUT_SLOT.ordinal()};
    private static final int[] slotsSides = new int[] {};
    /** The ItemStacks that hold the items currently being used in the grinder */
    private NonNullList<ItemStack> grinderItemStacks = NonNullList.<ItemStack>withSize(3, ItemStack.EMPTY);
    /** The number of ticks that the grinder will keep grinding */
    private int timeCanGrind;
    /** The number of ticks that a fresh copy of the currently-grinding item would keep the grinder grinding for */
    private int currentItemGrindTime;
    private int ticksGrindingItemSoFar;
    private int ticksPerItem;
    private String grinderCustomName;

 
    /**
     * Returns the number of slots in the inventory.
     */
    @Override
    public int getSizeInventory()
    {
        return grinderItemStacks.size();
    }

    /**
     * Returns the stack in slot i
     */
    @Override
    public ItemStack getStackInSlot(int index)
    {
        return grinderItemStacks.get(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    @Override
	public ItemStack decrStackSize(int index, int count)
    {
        return ItemStackHelper.getAndSplit(grinderItemStacks, index, count);
    }
    
    /**
     * Removes a stack from the given slot and returns it.
     */
    @Override
	public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(grinderItemStacks, index);
    }

//    /**
//     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
//     * like when you close a workbench GUI.
//     */
//    @Override
//    public ItemStack getStackInSlotOnClosing(int index)
//    {
//        if (grinderItemStacks.get(index) != ItemStack.EMPTY)
//        {
//            ItemStack itemstack = grinderItemStacks.get(index);
//            grinderItemStacks.get(index) = ItemStack.EMPTY;
//            return itemstack;
//        }
//        else
//        {
//            return ItemStack.EMPTY;
//        }
//    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        // DEBUG
        System.out.println("TileEntityGrinder setInventorySlotContents()");
        
        boolean isSameItemStackAlreadyInSlot = stack != ItemStack.EMPTY && stack.isItemEqual(grinderItemStacks.get(index)) && ItemStack.areItemStackTagsEqual(stack, grinderItemStacks.get(index));
        grinderItemStacks.set(index, stack);

        if (stack != ItemStack.EMPTY && stack.getCount() > getInventoryStackLimit())
        {
            stack.setCount(getInventoryStackLimit());
        }

        // if input slot, reset the grinding timers
        if (index == slotEnum.INPUT_SLOT.ordinal() && !isSameItemStackAlreadyInSlot)
        {
            ticksPerItem = timeToGrindOneItem(stack);
            ticksGrindingItemSoFar = 0;
            markDirty();
        }
    }

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    @Override
    public String getName()
    {
        return hasCustomName() ? grinderCustomName : "container.grinder";
    }

    /**
     * Returns true if this thing is named
     */
    @Override
    public boolean hasCustomName()
    {
        return grinderCustomName != null && grinderCustomName.length() > 0;
    }

    public void setCustomInventoryName(String parCustomName)
    {
        grinderCustomName = parCustomName;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        grinderItemStacks = NonNullList.<ItemStack>withSize(getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, grinderItemStacks);
        timeCanGrind = compound.getShort("GrindTime");
        ticksGrindingItemSoFar = compound.getShort("CookTime");
        ticksPerItem = compound.getShort("CookTimeTotal");

        if (compound.hasKey("CustomName", 8))
        {
            grinderCustomName = compound.getString("CustomName");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setShort("GrindTime", (short)timeCanGrind);
        compound.setShort("CookTime", (short)ticksGrindingItemSoFar);
        compound.setShort("CookTimeTotal", (short)ticksPerItem);
        ItemStackHelper.saveAllItems(compound, grinderItemStacks);

        if (hasCustomName())
        {
            compound.setString("CustomName", grinderCustomName);
        }
        
        return compound;
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * Grinder is grinding
     */
    public boolean grindingSomething()
    {
        return true;
    }

    // this function indicates whether container texture should be drawn
    @SideOnly(Side.CLIENT)
    public static boolean grindingSomething(IInventory inventory)
    {
        return inventory.getField(0) > 0;
    }

    @Override
    public void update()
    {
        boolean hasBeenGrinding = grindingSomething();
        boolean changedGrindingState = false;

        if (grindingSomething())
        {
            --timeCanGrind;
        }

        if (!world.isRemote)
        {
            // if something in input slot
            if (grinderItemStacks.get(slotEnum.INPUT_SLOT.ordinal()) != ItemStack.EMPTY)
            {                
                 // start grinding
                if (!grindingSomething() && canGrind())
                {
                    // DEBUG
                    System.out.println("TileEntityGrinder update() started grinding");
                    
                    timeCanGrind = 150;
    
                     if (grindingSomething())
                     {
                         changedGrindingState = true;
                     }
                }

                // continue grinding
                if (grindingSomething() && canGrind())
                {
                    // DEBUG
                    System.out.println("TileEntityGrinder update() continuing grinding");
                    
                    ++ticksGrindingItemSoFar;
                    
                    // check if completed grinding an item
                    if (ticksGrindingItemSoFar == ticksPerItem)
                    {
                        // DEBUG
                        System.out.println("Grinding completed another output cycle");
                        
                        ticksGrindingItemSoFar = 0;
                        ticksPerItem = timeToGrindOneItem(grinderItemStacks.get(0));
                        grindItem();
                        changedGrindingState = true;
                    }
                }
                else
                {
                    ticksGrindingItemSoFar = 0;
                }
            }

            // started or stopped grinding, update block to change to active or inactive model
            if (hasBeenGrinding != grindingSomething()) // the isGrinding() value may have changed due to call to grindItem() earlier
            {
                changedGrindingState = true;
                BlockGrinder.changeBlockBasedOnGrindingStatus(grindingSomething(), world, pos);
            }
        }

        if (changedGrindingState)
        {
            markDirty();
        }
    }

    public int timeToGrindOneItem(ItemStack parItemStack)
    {
        return 200;
    }

    /**
     * Returns true if the grinder can grind an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canGrind()
    {
        // if nothing in input slot
        if (grinderItemStacks.get(slotEnum.INPUT_SLOT.ordinal()) == ItemStack.EMPTY)
        {
            return false;
        }
        else // check if it has a grinding recipe
        {
            ItemStack itemStackToOutput = GrinderRecipes.instance().getGrindingResult(grinderItemStacks.get(slotEnum.INPUT_SLOT.ordinal()));
            if (itemStackToOutput == ItemStack.EMPTY) return false; // no valid recipe for grinding this item
            if (grinderItemStacks.get(slotEnum.OUTPUT_SLOT.ordinal()) == ItemStack.EMPTY) return true; // output slot is empty
            if (!grinderItemStacks.get(slotEnum.OUTPUT_SLOT.ordinal()).isItemEqual(itemStackToOutput)) return false; // output slot has different item occupying it
            // check if output slot is full
            int result = grinderItemStacks.get(slotEnum.OUTPUT_SLOT.ordinal()).getCount() + itemStackToOutput.getCount();
            return result <= getInventoryStackLimit() && result <= grinderItemStacks.get(slotEnum.OUTPUT_SLOT.ordinal()).getMaxStackSize();
        }
    }

    /**
     * Turn one item from the grinder source stack into the appropriate grinded item in the grinder result stack
     */
    public void grindItem()
    {
        if (canGrind())
        {
            ItemStack itemstack = GrinderRecipes.instance().getGrindingResult(grinderItemStacks.get(slotEnum.INPUT_SLOT.ordinal()));
            // check if output slot is empty
            if (grinderItemStacks.get(slotEnum.OUTPUT_SLOT.ordinal()) == ItemStack.EMPTY)
            {
                grinderItemStacks.set(slotEnum.OUTPUT_SLOT.ordinal(), itemstack.copy());
            }
            else if (grinderItemStacks.get(slotEnum.OUTPUT_SLOT.ordinal()).getItem() == itemstack.getItem())
            {
                grinderItemStacks.get(slotEnum.OUTPUT_SLOT.ordinal()).setCount(grinderItemStacks.get(slotEnum.OUTPUT_SLOT.ordinal()).getCount() + itemstack.getCount()); // Forge BugFix: Results may have multiple items
            }

            grinderItemStacks.get(slotEnum.INPUT_SLOT.ordinal()).setCount(grinderItemStacks.get(slotEnum.INPUT_SLOT.ordinal()).getCount() - 1);

            if (grinderItemStacks.get(slotEnum.INPUT_SLOT.ordinal()).getCount() <= 0)
            {
                grinderItemStacks.set(slotEnum.INPUT_SLOT.ordinal(), ItemStack.EMPTY);
            }
        }
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    @Override
	public boolean isUsableByPlayer(EntityPlayer player)
    {
        if (world.getTileEntity(pos) != this)
        {
            return false;
        }
        else
        {
            return player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
        }
    }
    @Override
    public void openInventory(EntityPlayer playerIn) {}

    @Override
    public void closeInventory(EntityPlayer playerIn) {}

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return index == slotEnum.INPUT_SLOT.ordinal() ? true : false; // can always put things in input (may not grind though) and can't put anything in output
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return side == EnumFacing.DOWN ? slotsBottom : (side == EnumFacing.UP ? slotsTop : slotsSides);
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side. Args: slot, item,
     * side
     */
    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
    {
        return isItemValidForSlot(index, itemStackIn);
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side. Args: slot, item,
     * side
     */
    @Override
    public boolean canExtractItem(int parSlotIndex, ItemStack parStack, EnumFacing parFacing)
    {
        return true;
    }

    @Override
    public String getGuiID()
    {
        return "blocksmith:grinder";
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        // DEBUG
        System.out.println("TileEntityGrinder createContainer()");
        return new ContainerGrinder(playerInventory, this);
    }

    @Override
    public int getField(int id)
    {
        switch (id)
        {
            case 0:
                return timeCanGrind;
            case 1:
                return currentItemGrindTime;
            case 2:
                return ticksGrindingItemSoFar;
            case 3:
                return ticksPerItem;
            default:
                return 0;
        }
    }

    @Override
    public void setField(int id, int value)
    {
        switch (id)
        {
            case 0:
                timeCanGrind = value;
                break;
            case 1:
                currentItemGrindTime = value;
                break;
            case 2:
                ticksGrindingItemSoFar = value;
                break;
            case 3:
                ticksPerItem = value;
                break;
        default:
            break;
        }
    }

    @Override
    public int getFieldCount()
    {
        return 4;
    }

    @Override
    public void clear()
    {
        for (int i = 0; i < grinderItemStacks.size(); ++i)
        {
            grinderItemStacks.set(i, ItemStack.EMPTY);
        }
    }

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}
}