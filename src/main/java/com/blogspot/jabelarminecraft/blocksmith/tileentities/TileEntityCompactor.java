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

import com.blogspot.jabelarminecraft.blocksmith.blocks.BlockCompactor;
import com.blogspot.jabelarminecraft.blocksmith.containers.ContainerCompactor;
import com.blogspot.jabelarminecraft.blocksmith.recipes.CompactorRecipes;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;

/**
 * @author jabelar
 *
 */
public class TileEntityCompactor extends TileEntityLockable implements ITickable, ISidedInventory
{
    // enumerate the slots
    public enum slotEnum 
    {
        INPUT_SLOT, OUTPUT_SLOT
    }
    protected static final int[] slotsTop = new int[] {slotEnum.INPUT_SLOT.ordinal()};
    protected static final int[] slotsBottom = new int[] {slotEnum.OUTPUT_SLOT.ordinal()};
    protected static final int[] slotsSides = new int[] {};
    /** The ItemStacks that hold the items currently being used in the compactor */
    protected ItemStack[] compactorItemStackArray = new ItemStack[2];
    /** The number of ticks that the compactor will keep compacting */
    protected int timeCanCompact;
    /** The number of ticks that a fresh copy of the currently-compacting item would keep the compactor compacting for */
    protected int currentItemCompactTime;
    protected int ticksCompactingItemSoFar;
    protected int ticksPerItem;
    protected String compactorCustomName;

    protected IItemHandler handlerTop = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.UP);
    protected IItemHandler handlerBottom = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.DOWN);
    protected IItemHandler handlerSide = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.WEST);

    
    /**
     * This controls whether the tile entity gets replaced whenever the block state is changed.
     * Normally only want this when block actually is replaced.
     */
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
	    return (oldState.getBlock() != newSate.getBlock());
	}

    /**
     * Returns the number of slots in the inventory.
     */
    @Override
	public int getSizeInventory()
    {
        return compactorItemStackArray.length;
    }

    /**
     * Returns the stack in slot i
     */
    @Override
	public ItemStack getStackInSlot(int index)
    {
        return compactorItemStackArray[index];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    @Override
	public ItemStack decrStackSize(int index, int count)
    {
        if (compactorItemStackArray[index] != null)
        {
            ItemStack itemstack;

            if (compactorItemStackArray[index].getCount() <= count)
            {
                itemstack = compactorItemStackArray[index];
                compactorItemStackArray[index] = null;
                return itemstack;
            }
            else
            {
                itemstack = compactorItemStackArray[index].splitStack(count);

                if (compactorItemStackArray[index].getCount() == 0)
                {
                    compactorItemStackArray[index] = null;
                }

                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

//    /**
//     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
//     * like when you close a workbench GUI.
//     */
//    @Override
//	public ItemStack getStackInSlotOnClosing(int index)
//    {
//        if (compactorItemStackArray[index] != null)
//        {
//            ItemStack itemstack = compactorItemStackArray[index];
//            compactorItemStackArray[index] = null;
//            return itemstack;
//        }
//        else
//        {
//            return null;
//        }
//    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    @Override
	public void setInventorySlotContents(int index, ItemStack stack)
    {
    	// DEBUG
    	System.out.println("TileEntityCompactor setInventorySlotContents()");
    	
        boolean isSameItemStackAlreadyInSlot = stack != null && stack.isItemEqual(compactorItemStackArray[index]) && ItemStack.areItemStackTagsEqual(stack, compactorItemStackArray[index]);
        compactorItemStackArray[index] = stack;

        if (stack != null && stack.getCount() > getInventoryStackLimit())
        {
            stack.setCount(getInventoryStackLimit());
        }

        // if input slot, reset the compacting timers
        if (index == slotEnum.INPUT_SLOT.ordinal() && !isSameItemStackAlreadyInSlot)
        {
            ticksPerItem = timeToCompactOneItem(stack);
            ticksCompactingItemSoFar = 0;
            markDirty();
        }
    }

//    /**
//     * Gets the name of this command sender (usually username, but possibly "Rcon")
//     */
//    @Override
//	public String getCommandSenderName()
//    {
//        return hasCustomName() ? compactorCustomName : "container.compactor";
//    }

    /**
     * Returns true if this thing is named
     */
    @Override
	public boolean hasCustomName()
    {
        return compactorCustomName != null && compactorCustomName.length() > 0;
    }

    public void setCustomInventoryName(String parCustomName)
    {
        compactorCustomName = parCustomName;
    }

    @Override
	public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        NBTTagList nbttaglist = compound.getTagList("Items", 10);
        compactorItemStackArray = new ItemStack[getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbtTagCompound = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbtTagCompound.getByte("Slot");

            if (b0 >= 0 && b0 < compactorItemStackArray.length)
            {
                compactorItemStackArray[b0] = new ItemStack(nbtTagCompound);
            }
        }

        timeCanCompact = compound.getShort("CompactTime");
        ticksCompactingItemSoFar = compound.getShort("CookTime");
        ticksPerItem = compound.getShort("CookTimeTotal");

        if (compound.hasKey("CustomName", 8))
        {
            compactorCustomName = compound.getString("CustomName");
        }
    }

    @Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setShort("CompactTime", (short)timeCanCompact);
        compound.setShort("CookTime", (short)ticksCompactingItemSoFar);
        compound.setShort("CookTimeTotal", (short)ticksPerItem);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < compactorItemStackArray.length; ++i)
        {
            if (compactorItemStackArray[i] != null)
            {
                NBTTagCompound nbtTagCompound = new NBTTagCompound();
                nbtTagCompound.setByte("Slot", (byte)i);
                compactorItemStackArray[i].writeToNBT(nbtTagCompound);
                nbttaglist.appendTag(nbtTagCompound);
            }
        }

        compound.setTag("Items", nbttaglist);

        if (hasCustomName())
        {
            compound.setString("CustomName", compactorCustomName);
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
     * Compactor is compacting
     */
    public boolean compactingSomething()
    {
        return true; // this is where you can add a condition like fuel or redstone power
    }

    // this function indicates whether container texture should be drawn
    @SideOnly(Side.CLIENT)
    public static boolean func_174903_a(IInventory parIInventory)
    {
        return true ; // parIInventory.getField(0) > 0;
    }

    @Override
	public void update()
    {
        boolean hasBeenCompacting = compactingSomething();
        boolean changedCompactingState = false;

        if (compactingSomething())
        {
            --timeCanCompact;
        }

        if (!world.isRemote)
        {
        	// if something in input slot
            if (compactorItemStackArray[slotEnum.INPUT_SLOT.ordinal()] != null)
            {            	
             	// start compacting
                if (!compactingSomething() && canCompact())
                {
//	            	// DEBUG
//	            	System.out.println("TileEntityCompactor update() started compacting");
	            	
	                timeCanCompact = 150;
	
	                 if (compactingSomething())
	                 {
	                     changedCompactingState = true;
	                 }
                }

                // continue compacting
                if (compactingSomething() && canCompact())
                {
//	            	// DEBUG
//	            	System.out.println("TileEntityCompactor update() continuing compacting");
	            	
                    ++ticksCompactingItemSoFar;
                    
                    // check if completed compacting an item
                    if (ticksCompactingItemSoFar == ticksPerItem)
                    {
//                    	// DEBUG
//                    	System.out.println("Compacting completed another output cycle");
                    	
                        ticksCompactingItemSoFar = 0;
                        ticksPerItem = timeToCompactOneItem(compactorItemStackArray[0]);
                        compactItem();
                        changedCompactingState = true;
                    }
                }
                else
                {
                    ticksCompactingItemSoFar = 0;
                }
            }

            // started or stopped compacting, update block to change to active or inactive model
            if (hasBeenCompacting != compactingSomething()) // the isCompacting() value may have changed due to call to compactItem() earlier
            {
                changedCompactingState = true;
                BlockCompactor.changeBlockBasedOnCompactingStatus(compactingSomething(), world, pos);
            }
        }

        if (changedCompactingState)
        {
            markDirty();
        }
    }

    public int timeToCompactOneItem(ItemStack parItemStack)
    {
        return 200;
    }

    /**
     * Returns true if the compactor can compact an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canCompact()
    {
    	ItemStack stackInOutputSlot = compactorItemStackArray[slotEnum.OUTPUT_SLOT.ordinal()];
    	ItemStack stackInInputSlot = compactorItemStackArray[slotEnum.INPUT_SLOT.ordinal()];
    			
    	// if nothing in input slot
        if (stackInInputSlot == null)
        {
            return false;
        }
        else // check if it has a compacting recipe
        {
            ItemStack itemStackToOutput = CompactorRecipes.instance().getCompactingResult(stackInInputSlot);
            if (itemStackToOutput == null) // no valid recipe for compacting this item
            {
            	return false;
            }
            if (stackInOutputSlot == null) // output slot is empty
            {
            	// check if enough of the input item (to allow recipes that consume multiple amounts)            }
            	if (stackInInputSlot.getCount() >= CompactorRecipes.instance().getInputAmount(stackInInputSlot))
            	{
//            		// DEBUG
//            		System.out.println("There is "+stackInInputSlot.stackSize+" in input slot and "+CompactorRecipes.instance().getInputAmount(stackInInputSlot)+" is needed");
            		return true;
            	}
            	else // not enough in input stack
            	{
//            		// DEBUG
//            		System.out.println("TileEntityCompactor canCompact() right item but not enough in input slot");
            		return false;
            	}
            }
            if (!stackInOutputSlot.isItemEqual(itemStackToOutput)) // output slot has different item occupying it
            {
            	return false;
            }
            // check if output slot is full
            int result = stackInOutputSlot.getCount() + itemStackToOutput.getCount();
            if (result <= getInventoryStackLimit() && result <= stackInOutputSlot.getMaxStackSize())
            {
            	// check if enough of the input item (to allow recipes that consume multiple amounts)            }
            	if (stackInInputSlot.getCount() >= CompactorRecipes.instance().getInputAmount(stackInInputSlot))
            	{
//            		// DEBUG
//            		System.out.println("There is "+stackInInputSlot.stackSize+" in input slot and "+CompactorRecipes.instance().getInputAmount(stackInInputSlot)+" is needed");
            		return true;
            	}
            	else // not enough in input stack
            	{
//            		// DEBUG
//            		System.out.println("TileEntityCompactor canCompact() right item but not enough in input slot");
            		return false;
            	}
            }
            else // no room to output
            {
            	return false;
            }
        }
    }

    /**
     * Turn one item from the compactor source stack into the appropriate compacted item in the compactor result stack
     */
    public void compactItem()
    {
        if (canCompact())
        {
            ItemStack itemstack = CompactorRecipes.instance().getCompactingResult(compactorItemStackArray[slotEnum.INPUT_SLOT.ordinal()]);

            // check if output slot is empty
            if (compactorItemStackArray[slotEnum.OUTPUT_SLOT.ordinal()] == null)
            {
                compactorItemStackArray[slotEnum.OUTPUT_SLOT.ordinal()] = itemstack.copy();
            }
            else if (compactorItemStackArray[slotEnum.OUTPUT_SLOT.ordinal()].getItem() == itemstack.getItem())
            {
                compactorItemStackArray[slotEnum.OUTPUT_SLOT.ordinal()].setCount(compactorItemStackArray[slotEnum.OUTPUT_SLOT.ordinal()].getCount() + itemstack.getCount()); // Forge BugFix: Results may have multiple items
            }

            // consume the number of input items based on recipe
            compactorItemStackArray[slotEnum.INPUT_SLOT.ordinal()].setCount(
            		compactorItemStackArray[slotEnum.INPUT_SLOT.ordinal()].getCount() 
            		- CompactorRecipes.instance().getInputAmount(compactorItemStackArray[slotEnum.INPUT_SLOT.ordinal()])
            		);

            if (compactorItemStackArray[slotEnum.INPUT_SLOT.ordinal()].getCount() <= 0)
            {
                compactorItemStackArray[slotEnum.INPUT_SLOT.ordinal()] = null;
            }
        }
    }

//    /**
//     * Do not make give this method the name canInteractWith because it clashes with Container
//     */
//    @Override
//	public boolean isUseableByPlayer(EntityPlayer playerIn)
//    {
//    	this.
//        return world.getTileEntity(pos) != this ? false : playerIn.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
//    }

    @Override
	public void openInventory(EntityPlayer playerIn) {}

    @Override
	public void closeInventory(EntityPlayer playerIn) {}

    @Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return index == slotEnum.INPUT_SLOT.ordinal() ? true : false; // can always put things in input (may not compact though) and can't put anything in output
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
        return "blocksmith:compactor";
    }

    @Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
    	// DEBUG
    	System.out.println("TileEntityCompactor createContainer()");
        return new ContainerCompactor(playerInventory, this);
    }

    @Override
	public int getField(int id)
    {
        switch (id)
        {
            case 0:
                return timeCanCompact;
            case 1:
                return currentItemCompactTime;
            case 2:
                return ticksCompactingItemSoFar;
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
                timeCanCompact = value;
                break;
            case 1:
                currentItemCompactTime = value;
                break;
            case 2:
                ticksCompactingItemSoFar = value;
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
        for (int i = 0; i < compactorItemStackArray.length; ++i)
        {
            compactorItemStackArray[i] = null;
        }
    }

    /* (non-Javadoc)
     * @see net.minecraft.inventory.IInventory#removeStackFromSlot(int)
     */
    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see net.minecraft.world.IWorldNameable#getName()
     */
    @Override
    public String getName()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            if (facing == EnumFacing.DOWN)
                return (T) handlerBottom;
            else if (facing == EnumFacing.UP)
                return (T) handlerTop;
            else
                return (T) handlerSide;
        return super.getCapability(capability, facing);
    }

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		// TODO Auto-generated method stub
		return false;
	}

}