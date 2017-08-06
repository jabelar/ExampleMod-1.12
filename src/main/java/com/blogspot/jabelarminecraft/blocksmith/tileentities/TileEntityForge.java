/**
 * 
 */
package com.blogspot.jabelarminecraft.blocksmith.tileentities;

import com.blogspot.jabelarminecraft.blocksmith.blocks.BlockForge;
import com.blogspot.jabelarminecraft.blocksmith.containers.ContainerForge;
import com.blogspot.jabelarminecraft.blocksmith.recipes.ForgeRecipes;
import com.blogspot.jabelarminecraft.blocksmith.slots.SlotForgeFuel;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author agilroy
 *
 */
public class TileEntityForge extends TileEntityLockable implements ITickable, ISidedInventory
{
    private static final int[] slotsTop = new int[] {0};
    private static final int[] slotsBottom = new int[] {2, 1};
    private static final int[] slotsSides = new int[] {1};
    /** The ItemStacks that hold the items currently being used in the forge */
    private NonNullList<ItemStack> forgeItemStacks = NonNullList.<ItemStack>withSize(4, ItemStack.EMPTY);
    /** The number of ticks that the forge will keep burning */
    private int forgeBurnTime;
    /** The number of ticks that a fresh copy of the currently-burning item would keep the forge burning for */
    private int currentItemBurnTime;
    private int ticksForgingItemSoFar;
    private int ticksPerItem;
    private String forgeCustomName;

    /**
     * Returns the number of slots in the inventory.
     */
    @Override
	public int getSizeInventory()
    {
        return forgeItemStacks.size();
    }

    /**
     * Returns the stack in slot i
     */
    @Override
	public ItemStack getStackInSlot(int index)
    {
        return forgeItemStacks.get(index);
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    @Override
	public ItemStack decrStackSize(int index, int count)
    {
        if (forgeItemStacks.get(index) != ItemStack.EMPTY)
        {
            ItemStack itemstack;

            if (forgeItemStacks.get(index).getCount() <= count)
            {
                itemstack = forgeItemStacks.get(index);
                forgeItemStacks.set(index, ItemStack.EMPTY);
                return itemstack;
            }
            else
            {
                itemstack = forgeItemStacks.get(index).splitStack(count);

                if (forgeItemStacks.get(index).getCount() == 0)
                {
                    forgeItemStacks.set(index, ItemStack.EMPTY);
                }

                return itemstack;
            }
        }
        else
        {
            return ItemStack.EMPTY;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    @Override
	public ItemStack removeStackFromSlot(int index)
    {
        if (forgeItemStacks.get(index) != ItemStack.EMPTY)
        {
            ItemStack itemstack = forgeItemStacks.get(index);
            forgeItemStacks.set(index, ItemStack.EMPTY);
            return itemstack;
        }
        else
        {
            return ItemStack.EMPTY;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    @Override
	public void setInventorySlotContents(int index, ItemStack stack)
    {
        boolean flag = stack != ItemStack.EMPTY && stack.isItemEqual(forgeItemStacks.get(index)) && ItemStack.areItemStackTagsEqual(stack, forgeItemStacks.get(index));
        forgeItemStacks.set(index, stack);

        if (stack != ItemStack.EMPTY && stack.getCount() > getInventoryStackLimit())
        {
            stack.setCount(getInventoryStackLimit());
        }

        if (index == 0 && !flag)
        {
            ticksPerItem = func_174904_a(stack);
            ticksForgingItemSoFar = 0;
            markDirty();
        }
    }

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    @Override
	public String getName()
    {
        return hasCustomName() ? forgeCustomName : "container.forge";
    }

    /**
     * Returns true if this thing is named
     */
    @Override
	public boolean hasCustomName()
    {
        return forgeCustomName != null && forgeCustomName.length() > 0;
    }

    public void setCustomInventoryName(String p_145951_1_)
    {
        forgeCustomName = p_145951_1_;
    }

    @Override
	public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        forgeItemStacks = NonNullList.<ItemStack>withSize(getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, forgeItemStacks);
        forgeBurnTime = compound.getShort("BurnTime");
        ticksForgingItemSoFar = compound.getShort("CookTime");
        ticksPerItem = compound.getShort("CookTimeTotal");
        currentItemBurnTime = getItemBurnTime(forgeItemStacks.get(1));

        if (compound.hasKey("CustomName", 8))
        {
            forgeCustomName = compound.getString("CustomName");
        }
    }

    @Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setShort("BurnTime", (short)forgeBurnTime);
        compound.setShort("CookTime", (short)ticksForgingItemSoFar);
        compound.setShort("CookTimeTotal", (short)ticksPerItem);
        ItemStackHelper.saveAllItems(compound, forgeItemStacks);

        if (hasCustomName())
        {
            compound.setString("CustomName", forgeCustomName);
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
     * Forge isBurning
     */
    public boolean isBurning()
    {
        return forgeBurnTime > 0;
    }

    @SideOnly(Side.CLIENT)
    public static boolean func_174903_a(IInventory p_174903_0_)
    {
        return p_174903_0_.getField(0) > 0;
    }

    /**
     * Updates the JList with a new model.
     */
    @Override
	public void update()
    {
        boolean flag = isBurning();
        boolean flag1 = false;

        if (isBurning())
        {
            --forgeBurnTime;
        }

        if (!world.isRemote)
        {
            if (!isBurning() && (forgeItemStacks.get(1) == ItemStack.EMPTY || forgeItemStacks.get(0) == ItemStack.EMPTY))
            {
                if (!isBurning() && ticksForgingItemSoFar > 0)
                {
                    ticksForgingItemSoFar = MathHelper.clamp(ticksForgingItemSoFar - 2, 0, ticksPerItem);
                }
            }
            else
            {
                if (!isBurning() && canSmelt())
                {
                    currentItemBurnTime = forgeBurnTime = getItemBurnTime(forgeItemStacks.get(1));

                    if (isBurning())
                    {
                        flag1 = true;

                        if (forgeItemStacks.get(1) != ItemStack.EMPTY)
                        {
                            forgeItemStacks.get(1).setCount(forgeItemStacks.get(1).getCount() - 1);

                            if (forgeItemStacks.get(1).getCount() == 0)
                            {
                                forgeItemStacks.set(1, forgeItemStacks.get(1).getItem().getContainerItem(forgeItemStacks.get(1)));
                            }
                        }
                    }
                }

                if (isBurning() && canSmelt())
                {
                    ++ticksForgingItemSoFar;

                    if (ticksForgingItemSoFar == ticksPerItem)
                    {
                        ticksForgingItemSoFar = 0;
                        ticksPerItem = func_174904_a(forgeItemStacks.get(0));
                        smeltItem();
                        flag1 = true;
                    }
                }
                else
                {
                    ticksForgingItemSoFar = 0;
                }
            }

            if (flag != isBurning())
            {
                flag1 = true;
                BlockForge.changeBlockStateContainer(isBurning(), world, pos);
            }
        }

        if (flag1)
        {
            markDirty();
        }
    }

    public int func_174904_a(ItemStack p_174904_1_)
    {
        return 200;
    }

    /**
     * Returns true if the forge can smelt an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canSmelt()
    {
        if (forgeItemStacks.get(0) == ItemStack.EMPTY)
        {
            return false;
        }
        else
        {
            ItemStack itemstack = ForgeRecipes.instance().getSmeltingResult(forgeItemStacks.get(0));
            if (itemstack == ItemStack.EMPTY) return false;
            if (forgeItemStacks.get(2) == ItemStack.EMPTY) return true;
            if (!forgeItemStacks.get(2).isItemEqual(itemstack)) return false;
            int result = forgeItemStacks.get(2).getCount() + itemstack.getCount();
            return result <= getInventoryStackLimit() && result <= forgeItemStacks.get(2).getMaxStackSize(); //Forge BugFix: Make it respect stack sizes properly.
        }
    }

    /**
     * Turn one item from the forge source stack into the appropriate smelted item in the forge result stack
     */
    public void smeltItem()
    {
        if (canSmelt())
        {
            ItemStack itemstack = ForgeRecipes.instance().getSmeltingResult(forgeItemStacks.get(0));

            if (forgeItemStacks.get(2) == ItemStack.EMPTY)
            {
                forgeItemStacks.set(2, itemstack.copy());
            }
            else if (forgeItemStacks.get(2).getItem() == itemstack.getItem())
            {
                forgeItemStacks.get(2).setCount(forgeItemStacks.get(2).getCount() + itemstack.getCount()); // Forge BugFix: Results may have multiple items
            }

            if (forgeItemStacks.get(0).getItem() == Item.getItemFromBlock(Blocks.SPONGE) && forgeItemStacks.get(0).getMetadata() == 1 && forgeItemStacks.get(1) != ItemStack.EMPTY && forgeItemStacks.get(1).getItem() == Items.BUCKET)
            {
                forgeItemStacks.set(1,  new ItemStack(Items.WATER_BUCKET));
            }

            forgeItemStacks.get(0).setCount(forgeItemStacks.get(0).getCount() + 1);

            if (forgeItemStacks.get(0).getCount() <= 0)
            {
                forgeItemStacks.set(0, ItemStack.EMPTY);
            }
        }
    }

    /**
     * Returns the number of ticks that the supplied fuel item will keep the forge burning, or 0 if the item isn't
     * fuel
     */
    @SuppressWarnings("deprecation")
    public static int getItemBurnTime(ItemStack parItemStack)
    {
        if (parItemStack == ItemStack.EMPTY)
        {
            return 0;
        }
        else
        {
            Item item = parItemStack.getItem();

            if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.AIR)
            {
                Block block = Block.getBlockFromItem(item);

                if (block == Blocks.WOODEN_SLAB)
                {
                    return 150;
                }

                if (block.getMaterial(block.getDefaultState()) == Material.WOOD)
                {
                    return 300;
                }

                if (block == Blocks.COAL_BLOCK)
                {
                    return 16000;
                }
            }

            if (item instanceof ItemTool && ((ItemTool)item).getToolMaterialName().equals("WOOD")) return 200;
            if (item instanceof ItemSword && ((ItemSword)item).getToolMaterialName().equals("WOOD")) return 200;
            if (item instanceof ItemHoe && ((ItemHoe)item).getMaterialName().equals("WOOD")) return 200;
            if (item == Items.STICK) return 100;
            if (item == Items.COAL) return 1600;
            if (item == Items.LAVA_BUCKET) return 20000;
            if (item == Item.getItemFromBlock(Blocks.SAPLING)) return 100;
            if (item == Items.BLAZE_ROD) return 2400;
            return net.minecraftforge.fml.common.registry.GameRegistry.getFuelValue(parItemStack);
        }
    }

    public static boolean isItemFuel(ItemStack p_145954_0_)
    {
        /**
         * Returns the number of ticks that the supplied fuel item will keep the forge burning, or 0 if the item isn't
         * fuel
         */
        return getItemBurnTime(p_145954_0_) > 0;
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

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    @Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return index == 2 ? false : (index != 1 ? true : isItemFuel(stack) || SlotForgeFuel.isItemBucket(stack));
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
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
    {
        if (direction == EnumFacing.DOWN && index == 1)
        {
            Item item = stack.getItem();

            if (item != Items.WATER_BUCKET && item != Items.BUCKET)
            {
                return false;
            }
        }

        return true;
    }

    @Override
	public String getGuiID()
    {
        return "minecraft:forge";
    }

    @Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new ContainerForge(playerInventory, this);
    }

    @Override
	public int getField(int id)
    {
        switch (id)
        {
            case 0:
                return forgeBurnTime;
            case 1:
                return currentItemBurnTime;
            case 2:
                return ticksForgingItemSoFar;
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
                forgeBurnTime = value;
                break;
            case 1:
                currentItemBurnTime = value;
                break;
            case 2:
                ticksForgingItemSoFar = value;
                break;
            case 3:
                ticksPerItem = value;
                break;
            default:
                System.out.println("TileEntityForge illegal index "+id+" in setField() method");
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
        for (int i = 0; i < forgeItemStacks.size(); ++i)
        {
            forgeItemStacks.set(i, ItemStack.EMPTY);
        }
    }
    
    /**
     * This controls whether the tile entity gets replaced whenever the block state is changed.
     * Normally only want this when block actually is replaced.
     */
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
	    return (oldState.getBlock() != newSate.getBlock());
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}
}