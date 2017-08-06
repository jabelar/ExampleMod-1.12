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

import com.blogspot.jabelarminecraft.blocksmith.blocks.BlockTanningRack;
import com.blogspot.jabelarminecraft.blocksmith.containers.ContainerTanningRack;
import com.blogspot.jabelarminecraft.blocksmith.recipes.TanningRackRecipes;
import com.blogspot.jabelarminecraft.blocksmith.registries.ItemRegistry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
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
public class TileEntityTanningRack extends TileEntityLockable implements ITickable, ISidedInventory
{
    // enumerate the slots
    public enum slotEnum 
    {
        INPUT_SLOT, OUTPUT_SLOT
    }
    private static final int[] slotsTop = new int[] {slotEnum.INPUT_SLOT.ordinal()};
    private static final int[] slotsBottom = new int[] {slotEnum.OUTPUT_SLOT.ordinal()};
    private static final int[] slotsSides = new int[] {};
    /** The ItemStacks that hold the items currently being used in the tanningRack */
//    private ItemStack[] tanningRackItemStackArray = new ItemStack[2];
    private NonNullList<ItemStack> tanningRackItemStacks = NonNullList.<ItemStack>withSize(3, ItemStack.EMPTY);
    /** The number of ticks that the tanningRack will keep tanning */
    private int timeCanGrind;
    /** The number of ticks that a fresh copy of the currently-tanning item would keep the tanningRack tanning for */
    private int currentItemGrindTime;
    private int ticksTanningItemSoFar;
    private int ticksPerItem;
    private String tanningRackCustomName;
   
    /**
     * Returns the number of slots in the inventory.
     */
    @Override
	public int getSizeInventory()
    {
        return tanningRackItemStacks.size();
    }

    /**
     * Returns the stack in slot i
     */
    @Override
	public ItemStack getStackInSlot(int index)
    {
        return tanningRackItemStacks.get(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    @Override
	public ItemStack decrStackSize(int index, int count)
    {
        return ItemStackHelper.getAndSplit(tanningRackItemStacks, index, count);
    }
    
    /**
     * Removes a stack from the given slot and returns it.
     */
    @Override
	public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(tanningRackItemStacks, index);
    }


//    /**
//     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
//     * like when you close a workbench GUI.
//     */
//    @Override
//	public ItemStack getStackInSlotOnClosing(int index)
//    {
//        if (tanningRackItemStacks[index] != null)
//        {
//            ItemStack itemstack = tanningRackItemStacks[index];
//            tanningRackItemStacks[index] = null;
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
    	System.out.println("TileEntityTanningRack setInventorySlotContents()");
    	
        boolean isSameItemStackAlreadyInSlot = stack != null && stack.isItemEqual(tanningRackItemStacks.get(index)) && ItemStack.areItemStackTagsEqual(stack, tanningRackItemStacks.get(index));
        tanningRackItemStacks.set(index, stack);

        if (stack != null && stack.getCount() > getInventoryStackLimit())
        {
            stack.setCount(getInventoryStackLimit());
        }

        // if input slot, reset the tanning timers
        if (index == slotEnum.INPUT_SLOT.ordinal() && !isSameItemStackAlreadyInSlot)
        {
            ticksPerItem = timeToTanOneItem(stack);
            ticksTanningItemSoFar = 0;
            markDirty();
        }
    }

    @Override
	public String getName()
    {
        return hasCustomName() ? tanningRackCustomName : "container.tanningRack";
    }

    /**
     * Returns true if this thing is named
     */
    @Override
	public boolean hasCustomName()
    {
        return tanningRackCustomName != null && tanningRackCustomName.length() > 0;
    }
    
    public void setCustomInventoryName(String parCustomName)
    {
        tanningRackCustomName = parCustomName;
    }

    @Override
	public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        tanningRackItemStacks = NonNullList.<ItemStack>withSize(getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, tanningRackItemStacks);
        timeCanGrind = compound.getShort("GrindTime");
        ticksTanningItemSoFar = compound.getShort("CookTime");
        ticksPerItem = compound.getShort("CookTimeTotal");

        if (compound.hasKey("CustomName", 8))
        {
            tanningRackCustomName = compound.getString("CustomName");
        }
    }

    @Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setShort("GrindTime", (short)timeCanGrind);
        compound.setShort("CookTime", (short)ticksTanningItemSoFar);
        compound.setShort("CookTimeTotal", (short)ticksPerItem);
        ItemStackHelper.saveAllItems(compound, tanningRackItemStacks);

        if (hasCustomName())
        {
            compound.setString("CustomName", tanningRackCustomName);
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
     * TanningRack is tanning
     */
    public boolean isTanning()
    {
        return true;
    }

    // this function indicates whether container texture should be drawn
    @SideOnly(Side.CLIENT)
    public static boolean isTanning(IInventory inventory)
    {
        return inventory.getField(0) > 0;
    }

    @Override
	public void update()
    {
        boolean hasBeenTanning = isTanning();
        boolean changedTanningState = false;

        if (isTanning())
        {
            --timeCanGrind;
        }

        if (!world.isRemote)
        {
        	// if something in input slot
            if (!tanningRackItemStacks.get(0).isEmpty())
            {            	
             	// start tanning
                if (!isTanning() && canTan())
                {
	            	// DEBUG
	            	System.out.println("TileEntityTanningRack update() started tanning");
	            	
	                timeCanGrind = 150;
	
	                 if (isTanning())
	                 {
	                     changedTanningState = true;
	                 }
                }

                // continue tanning
                if (isTanning() && canTan())
                {
//	            	// DEBUG
//	            	System.out.println("TileEntityTanningRack update() continuing tanning");
	            	
                    ++ticksTanningItemSoFar;
                    
                    // check if completed tanning an item
                    if (ticksTanningItemSoFar == ticksPerItem)
                    {
                    	// DEBUG
                    	System.out.println("Tanning completed another output cycle");
                    	
                        ticksTanningItemSoFar = 0;
                        ticksPerItem = timeToTanOneItem(tanningRackItemStacks.get(0));
                        tanItem();
                        changedTanningState = true;
                    }
                }
                else
                {
                    ticksTanningItemSoFar = 0;
                }
            }

            // started or stopped tanning, update block to change to active or inactive model
            if (hasBeenTanning != isTanning()) // the isTanning() value may have changed due to call to tanItem() earlier
            {
            	// DEBUG
            	System.out.println("Changed tanning state");
                changedTanningState = true;
            }
            
            // if leather result is in output slot display it
            if (tanningRackItemStacks.get(slotEnum.INPUT_SLOT.ordinal()) != null)
            {
                 BlockTanningRack.changeBlockBasedOnTanningStatus(6, world, pos);
            }
            else // display what is in input slot
            {
            	if (tanningRackItemStacks.get(slotEnum.INPUT_SLOT.ordinal()) != null)
	            {
            		Item inputItem = tanningRackItemStacks.get(slotEnum.INPUT_SLOT.ordinal()).getItem();
            		
	            	if (inputItem == ItemRegistry.COW_HIDE)
	            	{
	                	BlockTanningRack.changeBlockBasedOnTanningStatus(1, world, pos);
	            	}
	            	else if (inputItem == ItemRegistry.SHEEP_SKIN)
	            	{
	                	BlockTanningRack.changeBlockBasedOnTanningStatus(2, world, pos);
	            	}
	            	else if (inputItem == ItemRegistry.PIG_SKIN)
	            	{
	                	BlockTanningRack.changeBlockBasedOnTanningStatus(3, world, pos);
	            	}
	            	else if (inputItem == ItemRegistry.HORSE_HIDE)
	            	{
	                	BlockTanningRack.changeBlockBasedOnTanningStatus(4, world, pos);
	            	}
	            	else if (inputItem == Items.RABBIT_HIDE)
	            	{
	                	BlockTanningRack.changeBlockBasedOnTanningStatus(5, world, pos);
	            	}
            	}
            	else
            	{
                	BlockTanningRack.changeBlockBasedOnTanningStatus(0, world, pos);
            	}
            }
        }

        if (changedTanningState)
        {
            markDirty();
        }
    }

    public int timeToTanOneItem(ItemStack parItemStack)
    {
        return 200;
    }

    /**
     * Returns true if the tanningRack can tan an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canTan()
    {
    	// if nothing in input slot
        if (tanningRackItemStacks.get(slotEnum.INPUT_SLOT.ordinal()) == null)
        {
            return false;
        }
        else // check if it has a tanning recipe
        {
            ItemStack itemStackToOutput = TanningRackRecipes.instance().getTanningResult(tanningRackItemStacks.get(slotEnum.INPUT_SLOT.ordinal()));
            if (itemStackToOutput == null) return false; // no valid recipe for tanning this item
            if (tanningRackItemStacks.get(slotEnum.OUTPUT_SLOT.ordinal()) == null) return true; // output slot is empty
            if (!tanningRackItemStacks.get(slotEnum.OUTPUT_SLOT.ordinal()).isItemEqual(itemStackToOutput)) return false; // output slot has different item occupying it
            // check if output slot is full
            int result = tanningRackItemStacks.get(slotEnum.OUTPUT_SLOT.ordinal()).getCount() + itemStackToOutput.getCount();
            return result <= getInventoryStackLimit() && result <= tanningRackItemStacks.get(slotEnum.OUTPUT_SLOT.ordinal()).getMaxStackSize();
        }
    }

    /**
     * Turn one item from the tanningRack source stack into the appropriate taned item in the tanningRack result stack
     */
    public void tanItem()
    {
        if (canTan())
        {
            ItemStack itemstack = TanningRackRecipes.instance().getTanningResult(tanningRackItemStacks.get(slotEnum.INPUT_SLOT.ordinal()));

            // check if output slot is empty
            if (tanningRackItemStacks.get(slotEnum.OUTPUT_SLOT.ordinal()) == null)
            {
                tanningRackItemStacks.set(slotEnum.OUTPUT_SLOT.ordinal(), itemstack.copy());
            }
            else if (tanningRackItemStacks.get(slotEnum.OUTPUT_SLOT.ordinal()).getItem() == itemstack.getItem())
            {
                tanningRackItemStacks.get(slotEnum.OUTPUT_SLOT.ordinal()).setCount(tanningRackItemStacks.get(slotEnum.OUTPUT_SLOT.ordinal()).getCount() + itemstack.getCount()); // Forge BugFix: Results may have multiple items
            }

            tanningRackItemStacks.get(slotEnum.INPUT_SLOT.ordinal()).setCount(tanningRackItemStacks.get(slotEnum.INPUT_SLOT.ordinal()).getCount() - 1);

            if (tanningRackItemStacks.get(slotEnum.INPUT_SLOT.ordinal()).getCount() <= 0)
            {
                tanningRackItemStacks.set(slotEnum.INPUT_SLOT.ordinal(), ItemStack.EMPTY);
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
        return index == slotEnum.INPUT_SLOT.ordinal() ? true : false; // can always put things in input (may not tan though) and can't put anything in output
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
        return "blocksmith:tanningRack";
    }

    @Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
    	// DEBUG
    	System.out.println("TileEntityTanningRack createContainer()");
        return new ContainerTanningRack(playerInventory, this);
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
                return ticksTanningItemSoFar;
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
                ticksTanningItemSoFar = value;
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
        for (int i = 0; i < tanningRackItemStacks.size(); ++i)
        {
            tanningRackItemStacks.set(i, ItemStack.EMPTY);
        }
    }

	@Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.tanningRackItemStacks)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }
}