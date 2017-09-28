/**
    Copyright (C) 2017 by jabelar

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
package com.blogspot.jabelarminecraft.examplemod.items.fluidcontainers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.blogspot.jabelarminecraft.examplemod.fluids.FluidHandlerSlimeBag;
import com.blogspot.jabelarminecraft.examplemod.init.ModFluids;
import com.blogspot.jabelarminecraft.examplemod.utilities.ItemStackCopy;
import com.blogspot.jabelarminecraft.examplemod.utilities.Utilities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.DispenseFluidContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.items.IItemHandler;

// TODO: Auto-generated Javadoc
@SuppressWarnings("deprecation")
public class ItemSlimeBag extends Item
{
	private final int CAPACITY = Fluid.BUCKET_VOLUME;
	private final ItemStack emptyStack = new ItemStack(this);
	
	/**
	 * Instantiates a new item slime bag.
	 */
	public ItemSlimeBag() 
	{
		Utilities.setItemName(this, "slime_bag");
		setCreativeTab(CreativeTabs.MISC);
		setMaxStackSize(1);
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, DispenseFluidContainer.getInstance());
     
        // DEBUG
		System.out.println("Constructing ItemSlimeBag");
	}
	
    /* (non-Javadoc)
     * @see net.minecraft.item.Item#initCapabilities(net.minecraft.item.ItemStack, net.minecraft.nbt.NBTTagCompound)
     */
    @Override
    public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable NBTTagCompound nbt)
    {
    	// DEBUG
    	System.out.println("initCapabilities for ItemSlimeBag with NBT = "+stack.getTagCompound()+" and Cap NBT = "+nbt);
    	
        return new FluidHandlerSlimeBag(stack, CAPACITY);
    }

	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#getSubItems(net.minecraft.creativetab.CreativeTabs, net.minecraft.util.NonNullList)
	 */
	@Override
	public void getSubItems(@Nullable final CreativeTabs tab, final NonNullList<ItemStack> subItems) 
	{
		if (!this.isInCreativeTab(tab)) return;

		subItems.add(emptyStack);

		final FluidStack fluidStack = new FluidStack(ModFluids.SLIME, CAPACITY);
		final ItemStack stack = new ItemStack(this);
		final IFluidHandlerItem fluidHandler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
		if (fluidHandler != null)
		{
			final int fluidFillAmount = fluidHandler.fill(fluidStack, true);
			if (fluidFillAmount == fluidStack.amount) 
			{
				final ItemStack filledStack = fluidHandler.getContainer();
				subItems.add(filledStack);	
				
				// DEBUG
				System.out.println("Filled bag and adding as sub-item = "+filledStack+" with amount = "+FluidUtil.getFluidContained(filledStack).amount);
			}
			else
			{
				// DEBUG
				System.out.println("Failed to add filled sub-item because amounts didn't match, fillAmount = "+fluidFillAmount);
			}
		}
		else
		{
			// DEBUG
			System.out.println("Failed to add filled sub-item because fluid handler was null");
		}
	}

	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#getItemStackDisplayName(net.minecraft.item.ItemStack)
	 */
	@Override
	public String getItemStackDisplayName(final ItemStack stack) 
	{
		final FluidStack fluidStack = FluidUtil.getFluidContained(stack);
		String unlocalizedName = this.getUnlocalizedNameInefficiently(stack);

		// If the bucket is empty, translate the unlocalised name directly
		if (fluidStack == null) 
		{
			unlocalizedName += ".name";
		}
		else
		{
			unlocalizedName += ".filled.name";
		}

		return I18n.translateToLocal(unlocalizedName).trim();
	}
	
	public static ItemStack empty(ItemStack stack)
	{
		if (stack.getItem() instanceof ItemSlimeBag)
		{
			FluidStack fluidStack = FluidUtil.getFluidContained(stack);
			if (fluidStack != null)
			{
				fluidStack.amount = 0;
			}
		}
		
		return stack;	
	}


    /**
     * Called when the equipped item is right clicked.
     *
     * @param parWorld the par world
     * @param parPlayer the par player
     * @param parHand the par hand
     * @return the action result
     */
    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World parWorld, @Nonnull EntityPlayer parPlayer, @Nonnull EnumHand parHand)
    {
    	// DEBUG
    	System.out.println("onItemRightClick for ItemSlimeBag for hand = "+parHand);
    
        ItemStack itemStack = parPlayer.getHeldItem(parHand);

        // clicked on a block?
        RayTraceResult mop = rayTrace(parWorld, parPlayer, true);

        if(mop == null || mop.typeOfHit != RayTraceResult.Type.BLOCK)
        {
            return ActionResult.newResult(EnumActionResult.PASS, itemStack);
        }

        // DEBUG
        System.out.println("Slime bag used");
        
        BlockPos clickPos = mop.getBlockPos();
                
        FluidStack fluidStack = getFluidStack(itemStack);
            	
        // can we place liquid there?
        if (parWorld.isBlockModifiable(parPlayer, clickPos))
        {
            // the block adjacent to the side we clicked on
            BlockPos targetPos = clickPos.offset(mop.sideHit);

            // can the player place there?
            if (parPlayer.canPlayerEdit(targetPos, mop.sideHit, itemStack))
            {
                if (fluidStack == null || fluidStack.amount <= 0)
                {
                	// DEBUG
                	System.out.println("Fluid stack is empty so try to fill");
                	
                	return tryFill(parWorld, parPlayer, mop, itemStack);
                }
                else
                {     
                    // DEBUG
                    System.out.println("Fluid stack is not empty so try to place");

                	return tryPlace(parWorld, parPlayer, targetPos, itemStack);               	
                }
            }
         }
        
        // DEBUG
        System.out.println("Failed to place fluid");

        // couldn't place liquid there
        return ActionResult.newResult(EnumActionResult.PASS, itemStack);
    }
    
    /**
     * Try fill.
     *
     * @param parWorld the par world
     * @param parPlayer the par player
     * @param parRayTraceTarget the par ray trace target
     * @param parStack the par stack
     * @return the action result
     */
    public ActionResult<ItemStack> tryFill(World parWorld, EntityPlayer parPlayer, RayTraceResult parRayTraceTarget, ItemStack parStack)
    {
    	BlockPos pos = parRayTraceTarget.getBlockPos();

        FluidActionResult filledResult = FluidUtil.tryPickUpFluid(parStack, parPlayer, parWorld, pos, parRayTraceTarget.sideHit);
        if (filledResult.isSuccess())
        {
        	// DEBUG
        	System.out.println("Successful at picking up fluid item stack = "+filledResult.getResult());
        	
            return ActionResult.newResult(EnumActionResult.SUCCESS, filledResult.getResult());
        }
        else
        {
            // DEBUG
        	System.out.println("Not successful at picking up fluid");
            return ActionResult.newResult(EnumActionResult.FAIL, parStack);
        }
    }
    /**
     * Attempts to pick up a fluid in the world and put it in an empty container item.
     *
     * @param emptyContainer The empty container to fill.
     *                       Will not be modified directly, if modifications are necessary a modified copy is returned in the result.
     * @param playerIn       The player filling the container. Optional.
     * @param worldIn        The world the fluid is in.
     * @param pos            The position of the fluid in the world.
     * @param side           The side of the fluid that is being drained.
     * @return a {@link FluidActionResult} holding the result and the resulting container.
     */
    @Nonnull
    public static FluidActionResult tryPickUpFluid(@Nonnull ItemStack emptyContainer, @Nullable EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side)
    {
        if (emptyContainer.isEmpty() || worldIn == null || pos == null)
        {
            return FluidActionResult.FAILURE;
        }

        IBlockState state = worldIn.getBlockState(pos);
        Block block = state.getBlock();

        if (block instanceof IFluidBlock || block instanceof BlockLiquid)
        {
            IFluidHandler targetFluidHandler = FluidUtil.getFluidHandler(worldIn, pos, side);
            if (targetFluidHandler != null)
            {
                return tryFillContainer(emptyContainer, targetFluidHandler, Integer.MAX_VALUE, playerIn, true);
            }
        }
        return FluidActionResult.FAILURE;
    } 
    

    /**
     * Fill a container from the given fluidSource.
     *
     * @param parStack   The container to be filled. Will not be modified.
     *                    Separate handling must be done to reduce the stack size, stow containers, etc, on success.
     *                    See {@link  #tryFillContainerAndStow(ItemStack, IFluidHandler, IItemHandler, int, EntityPlayer)}.
     * @param fluidSource The fluid handler to be drained.
     * @param maxAmount   The largest amount of fluid that should be transferred.
     * @param player      The player to make the filling noise. Pass null for no noise.
     * @param doFill      true if the container should actually be filled, false if it should be simulated.
     * @return a {@link FluidActionResult} holding the filled container if successful.
     */
    @Nonnull
    public static FluidActionResult tryFillContainer(@Nonnull ItemStack parStack, IFluidHandler fluidSource, int maxAmount, @Nullable EntityPlayer player, boolean doFill)
    {
//        ItemStack containerCopy = ItemHandlerHelper.copyStackWithSize(container, 1); // do not modify the input
    	ItemStackCopy.store(parStack);
    	IFluidHandlerItem containerFluidHandler = FluidUtil.getFluidHandler(parStack);
        if (containerFluidHandler != null)
        {
            FluidStack simulatedTransfer = FluidUtil.tryFluidTransfer(containerFluidHandler, fluidSource, maxAmount, false);
            if (simulatedTransfer != null)
            {
                if (doFill)
                {
                    FluidUtil.tryFluidTransfer(containerFluidHandler, fluidSource, maxAmount, true);
                    if (player != null)
                    {
                        SoundEvent soundevent = simulatedTransfer.getFluid().getFillSound(simulatedTransfer);
                        player.playSound(soundevent, 1f, 1f);
                    }
                }
                else
                {
                    containerFluidHandler.fill(simulatedTransfer, true);
                }

                ItemStack resultContainer = containerFluidHandler.getContainer();
                return new FluidActionResult(resultContainer);
            }
        }
        return FluidActionResult.FAILURE;
    }

    
    /**
     * Try place.
     *
     * @param parWorld the par world
     * @param parPlayer the par player
     * @param pos the pos
     * @param parStack the par stack
     * @return the action result
     */
    public ActionResult<ItemStack> tryPlace(World parWorld, EntityPlayer parPlayer, BlockPos pos, ItemStack parStack)
    {
    	FluidStack fluidStack = getFluidStack(parStack);

        // try placing liquid
        FluidActionResult result = FluidUtil.tryPlaceFluid(parPlayer, parWorld, pos, parStack, fluidStack);

        // DEBUG
        System.out.println("Tried placing fluid with result success = "+result.isSuccess()+" and itemstack = "+result.getResult());
        
        if (result.isSuccess())
        {
	        if (!parPlayer.capabilities.isCreativeMode)
	        {
	            // DEBUG
	            System.out.println("Not in creative so draining container");
	
	            // success!
	            parPlayer.addStat(StatList.getObjectUseStats(this));
	
	            parStack.shrink(1);
	
	            // DEBUG
	        	System.out.println("Adding empty slime bag to player inventory = "+emptyStack);
	        	
	            // add empty bucket to player inventory
	            return ActionResult.newResult(EnumActionResult.SUCCESS, emptyStack);
	        }
	        else
	        {
	        	// DEBUG
	        	System.out.println("Placing fluid was a success");
	        	
	            return ActionResult.newResult(EnumActionResult.SUCCESS, parStack);
	        }
        }
        else
        {
        	// DEBUG
        	System.out.println("Placing fluid was not a success");
        	
        	return ActionResult.newResult(EnumActionResult.FAIL, parStack);
        }
    }

    /**
     * Gets the fluid stack.
     *
     * @param container the container
     * @return the fluid stack
     */
    @Nullable
	public FluidStack getFluidStack(final ItemStack container) 
    {
		if (container.hasTagCompound() && container.getTagCompound().hasKey(FluidHandlerItemStack.FLUID_NBT_KEY)) {
			return FluidStack.loadFluidStackFromNBT(container.getTagCompound().getCompoundTag(FluidHandlerItemStack.FLUID_NBT_KEY));
		}
		return null;
//		return FluidUtil.getFluidContained(container);
	}
    
    /**
     * If this function returns true (or the item is damageable), the ItemStack's NBT tag will be sent to the client.
     */
    @Override
	public boolean getShareTag()
    {
        return true;
    }
    @Override
	public NBTTagCompound getNBTShareTag(ItemStack stack)
    {
    	// DEBUG
    	System.out.println("tag compound = "+stack.getTagCompound());
    	
        return stack.getTagCompound();
    }

 }
