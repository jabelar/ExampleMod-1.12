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
import com.blogspot.jabelarminecraft.examplemod.utilities.Utilities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.DispenseFluidContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fluids.capability.wrappers.BlockLiquidWrapper;
import net.minecraftforge.fluids.capability.wrappers.BlockWrapper;
import net.minecraftforge.fluids.capability.wrappers.FluidBlockWrapper;

// TODO: Auto-generated Javadoc
@SuppressWarnings("deprecation")
public class ItemSlimeBag extends Item
{
	private final int CAPACITY = Fluid.BUCKET_VOLUME;
	private final ItemStack EMPTY_STACK = new ItemStack(this);
	
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
//    	// DEBUG
//    	System.out.println("initCapabilities for ItemSlimeBag with NBT = "+stack.getTagCompound()+" and Cap NBT = "+nbt);
    	
        return new FluidHandlerSlimeBag(stack, CAPACITY);
    }

	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#getSubItems(net.minecraft.creativetab.CreativeTabs, net.minecraft.util.NonNullList)
	 */
	@Override
	public void getSubItems(@Nullable final CreativeTabs tab, final NonNullList<ItemStack> subItems) 
	{
		if (!this.isInCreativeTab(tab)) return;

		subItems.add(EMPTY_STACK);

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
		String unlocalizedName = this.getUnlocalizedNameInefficiently(stack);
        IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(stack);
		FluidStack fluidStack = fluidHandler.getTankProperties()[0].getContents();

		// If the bucket is empty, translate the unlocalised name directly
		if (fluidStack == null || fluidStack.amount <= 0) 
		{
			unlocalizedName += ".name";
		}
		else
		{
			unlocalizedName += ".filled.name";
		}

		return I18n.translateToLocal(unlocalizedName).trim();
	}
	
	/**
	 * Empty.
	 *
	 * @param stack the stack
	 * @return the item stack
	 */
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
            	
        // can we place block there?
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
                	System.out.println("Fluid stack in item is empty so try to fill");
                	
                	return tryFillAlt(parWorld, parPlayer, mop, itemStack);
                }
                else
                {     
                    // DEBUG
                    System.out.println("Fluid stack is not empty so try to place");

                	return tryPlaceAlt(parWorld, parPlayer, targetPos, itemStack);               	
                }
            }
            else // player cannot edit
            {
    	        // DEBUG
    	        System.out.println("Failed to place fluid because player cannot edit");
    	
    	        // couldn't place liquid there
    	        return ActionResult.newResult(EnumActionResult.FAIL, itemStack);
            }
         }
        else // cannot place blocks at that location
        {
	        // DEBUG
	        System.out.println("Failed to place fluid because location not modifiable");
	
	        // couldn't place liquid there
	        return ActionResult.newResult(EnumActionResult.FAIL, itemStack);
        }
    }
    
    /**
     * Try place alt.
     *
     * @param parWorld the par world
     * @param parPlayer the par player
     * @param parPos the par pos
     * @param parStack the par stack
     * @return the action result
     */
    public ActionResult<ItemStack> tryPlaceAlt(World parWorld, EntityPlayer parPlayer, BlockPos parPos, ItemStack parStack)
    {
    	ActionResult<ItemStack> resultPass = new ActionResult<ItemStack>(EnumActionResult.PASS, parStack);
    	ActionResult<ItemStack> resultFail = new ActionResult<ItemStack>(EnumActionResult.FAIL, parStack);
    	
        if (parWorld == null || parPos == null) // not valid location to attempt to place
        {
        	// DEBUG
        	System.out.println("not valid location to place at");
        	
            return resultFail;
        }
        else // valid location to attempt to place
        {
        	// DEBUG
        	System.out.println("Valid location to place at");
        	
	        IFluidHandlerItem containerFluidHandler = FluidUtil.getFluidHandler(parStack);
	        
	        if (containerFluidHandler == null) // itemstack not really a fluid handler
	        {
	        	// DEBUG
	        	System.out.println("Item stack not really a fluid handling container");
	        	
	        	return resultFail;
	        }
	        else // itemstack is a valid fluid handler
	        {
	        	// DEBUG
	        	System.out.println("Item stack has a fluid handler");
	        	
		    	FluidStack containerFluidStack = getFluidStack(parStack);
		    	
		    	if (containerFluidStack == null || containerFluidStack.amount <= 0) // not actual fluid stack in container
		    	{
		    		// DEBUG
		    		System.out.println("No actual fluid in container");
		    		
		    		return resultFail;
		    	}
		    	else // there is actual fluid stack in container
		    	{
		    		// DEBUG
		    		System.out.println("There is a fluid stack in the container");
		    		
		            Fluid fluid = containerFluidStack.getFluid();
		            if (fluid == null ) // no fluid associated with fluid stack
		            {
		            	// DEBUG
		            	System.out.println("Malformed fluid stack has null fluid");
		            	
		                return resultFail;
		            }
		            else // fluid associated with fluid stack
		            {
		            	if (!fluid.canBePlacedInWorld()) // fluid cannot be placed in world
		            	{
		            		// DEBUG
		            		System.out.println("Fluid type doesn't allow placement in world");
		            		
		            		return resultFail;
		            	}
		            	else // fluid can be placed in world
		            	{
		            		// DEBUG
		            		System.out.println("Fluid type allows placement in world");
		            		
		                    // check that we can place the fluid at the destination
		                    IBlockState destBlockState = parWorld.getBlockState(parPos);
		                    if (!parWorld.isAirBlock(parPos) && destBlockState.getMaterial().isSolid() && !destBlockState.getBlock().isReplaceable(parWorld, parPos))
		                    {
		                    	// DEBUG
		                    	System.out.println("Location is not replaceable");
		                    	
		                        return resultFail; // Non-air, solid, unreplacable block. We can't put fluid here.
		                    }
		                    else // location is placeable
		                    {
		                    	// DEBUG
		                    	System.out.println("Location is replaceable");
		                    	
		                        if (parWorld.provider.doesWaterVaporize() && fluid.doesVaporize(containerFluidStack))
		                        {
		                                fluid.vaporize(parPlayer, parWorld, parPos, containerFluidStack);
		                                return ActionResult.newResult(EnumActionResult.SUCCESS, parStack);
		                        }
		                        else // fluid does not vaporize
		                        {
		                            // This fluid handler places the fluid block when filled
		                            Block blockToPlace = fluid.getBlock();
		                            IFluidHandler blockFluidHandler;
		                            if (blockToPlace instanceof IFluidBlock)
		                            {
		                                blockFluidHandler = new FluidBlockWrapper((IFluidBlock) blockToPlace, parWorld, parPos);
		                            }
		                            else if (blockToPlace instanceof BlockLiquid)
		                            {
		                                blockFluidHandler = new BlockLiquidWrapper((BlockLiquid) blockToPlace, parWorld, parPos);
		                            }
		                            else
		                            {
		                                blockFluidHandler = new BlockWrapper(blockToPlace, parWorld, parPos);
		                            }
		                            
		                            // actually transfer fluid
		                            int blockCapacity = blockFluidHandler.getTankProperties()[0].getCapacity();
		                            int amountInContainer = containerFluidStack.amount;
		                            
		                            FluidStack blockFluidStack = blockFluidHandler.getTankProperties()[0].getContents();
		                            if (blockFluidStack == null)
		                            {
		                            	// DEBUG
		                            	System.out.println("Block fluid stack is null");
		                            	
		                            	return resultFail;
		                            }
		                            else // non-null fluid stack
		                            {
			                            // DEBUG
			                            System.out.println("Before transferring fluids amount in container = "+amountInContainer+" and block capacity = "+blockCapacity);
			                            
			                            // transfer amounts and handle cases of differences between amounts and capacities
			                            if (amountInContainer > blockCapacity) // more than enough fluid to fill block
			                            {
			                            	containerFluidStack.amount -= blockCapacity;
			                            	blockFluidStack.amount = blockCapacity;
			                            }
			                            else // all fluid in container can fit within block
			                            {
			                            	blockFluidStack.amount = amountInContainer;
			                            	containerFluidStack.amount = 0;
			                            }
			                            
			                            // DEBUG
			                            System.out.println("After transferring amount in container = "+containerFluidStack.amount+" and amount in block = "+blockFluidStack.amount);
			                            
		                                SoundEvent soundevent = fluid.getEmptySound(containerFluidStack);
		                                parWorld.playSound(parPlayer, parPos, soundevent, SoundCategory.BLOCKS, 1.0F, 1.0F);
	
		                    	        if (!parPlayer.capabilities.isCreativeMode)
		                    	        {
		                    	            // DEBUG
		                    	            System.out.println("Not in creative so draining container");
		                    	
		                    	            // success!
		                    	            parPlayer.addStat(StatList.getObjectUseStats(this));
		                    	
		                    	            // clamp value to non-negative
		                    	            if (containerFluidStack.amount <= 0)
		                    	            {
		                    	            	containerFluidStack.amount = 0; 
		                    	            	
		                    	            	// DEBUG
		                    	            	System.out.println("fully drained the container so returning empty container");
		                    	            }
	                    	            	
			    							// update tag data
	                    	            	updateFluidNBT(parStack, containerFluidStack);
			    					        
			    					        // send packet to update player
	                    	            	sendUpdatePacketToClient(parPlayer);
			    							
			    							// DEBUG
			    							System.out.println("After transfer block fluid stack = "+blockFluidStack.getFluid()+" "+blockFluidStack.amount+" and container fluid stack now = "+containerFluidStack.getFluid()+" "+containerFluidStack.amount);
			            					                    	        	
			                    	        parWorld.setBlockState(parPos, blockToPlace.getDefaultState());

		                    	        	// DEBUG
		                    	        	System.out.println("Placing fluid was a success");
		                    	        	
			                                return ActionResult.newResult(EnumActionResult.SUCCESS, containerFluidHandler.getContainer());
		                    	        }
		                    	        else // in creative mode so don't use up stack
		                    	        {
		                    	        	// restore amount
		                    	        	containerFluidStack.amount = amountInContainer;
		                    	        	
									        parWorld.setBlockState(parPos, blockToPlace.getDefaultState());
									
									    	// DEBUG
									    	System.out.println("Placing fluid was a success");
									    	
		                    	        	return resultPass; // not really sure why fail, but consistent with universal bucket
		                    	        }
		                            }
		                        }
		                    }
		            	}
		            }
		    	}
	        }
        }
    }
   
    private void sendUpdatePacketToClient(EntityPlayer parPlayer) 
    {
    	
		if (parPlayer instanceof EntityPlayerMP)
		{
			// DEBUG
			System.out.println("Sending player inventory update");
	        ((EntityPlayerMP)parPlayer).connection.sendPacket(new SPacketHeldItemChange(parPlayer.inventory.currentItem));
		}
		else
		{
			// do nothing
		}		
	}

	private void updateFluidNBT(ItemStack parItemStack, FluidStack parFluidStack) 
    {
        if (!parItemStack.hasTagCompound())
        {
            parItemStack.setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound fluidTag = new NBTTagCompound();
        parFluidStack.writeToNBT(fluidTag);
        parItemStack.getTagCompound().setTag(FluidHandlerItemStack.FLUID_NBT_KEY, fluidTag);		
		
		// DEBUG
		System.out.println("Wrote fluid tag to container item stack = "+fluidTag);
	}

	/**
	 * Gets the matching fluid stack.
	 *
	 * @param sourceHandler the source handler
	 * @param parFluid the par fluid
	 * @return the matching fluid stack
	 */
	@Nullable
    public FluidStack getMatchingFluidStack(IFluidHandler sourceHandler, Fluid parFluid)
    {
    	// Theoretically a tank may contain mulitple fluid stacks
    	// grab first one that matches fluid type
    	IFluidTankProperties[] tankProperties = sourceHandler.getTankProperties();
    	FluidStack result = null;
    	for (int i=0; i < tankProperties.length; i++)
    	{
    		if (tankProperties[i].getContents().getFluid() == parFluid)
    		{
    			result = tankProperties[i].getContents();
    		}
    		else
    		{
    			// do nothing
    		}
    	}
    	
    	return result;
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
	}
    
    /**
     * If this function returns true (or the item is damageable), the ItemStack's NBT tag will be sent to the client.
     *
     * @return the share tag
     */
    @Override
	public boolean getShareTag()
    {
        return true;
    }
    
    /* (non-Javadoc)
     * @see net.minecraft.item.Item#getNBTShareTag(net.minecraft.item.ItemStack)
     */
    @Override
	public NBTTagCompound getNBTShareTag(ItemStack stack)
    {
    	// DEBUG
    	System.out.println("tag compound = "+stack.getTagCompound());
    	
        return stack.getTagCompound();
    }

    
    /**
     * Try fill alt.
     *
     * @param parWorld the par world
     * @param parPlayer the par player
     * @param mop the mop
     * @param parContainerStack the par container stack
     * @return the action result
     */
    public ActionResult<ItemStack> tryFillAlt(World parWorld, EntityPlayer parPlayer, RayTraceResult mop, ItemStack parContainerStack)
    {
    	ActionResult<ItemStack> resultPass = new ActionResult<ItemStack>(EnumActionResult.PASS, parContainerStack);
    	ActionResult<ItemStack> resultFail = new ActionResult<ItemStack>(EnumActionResult.FAIL, parContainerStack);
    	BlockPos blockPos = mop.getBlockPos();
    	
    	if (parWorld == null || blockPos == null || parContainerStack.isEmpty())
    	{
    		// DEBUG
    		System.out.println("invalid parameters (null or empty");
    		
    		return resultPass;
    	}
    	else // parameters are valid
    	{
    		// that there is fluid or liquid block at the position
	        Block block = parWorld.getBlockState(blockPos).getBlock();
	        if (block instanceof IFluidBlock || block instanceof BlockLiquid)
	        {
	            IFluidHandler sourceFluidHandler = FluidUtil.getFluidHandler(parWorld, blockPos, mop.sideHit);
	            if (sourceFluidHandler != null) // valid fluid block
	            {
		        	// DEBUG
		        	System.out.println("Found valid fluid block");
		        	
	            	IFluidHandlerItem containerFluidHandler = FluidUtil.getFluidHandler(parContainerStack);
	            	if (containerFluidHandler != null) // valid fluid item
	            	{
	            		// DEBUG
	            		System.out.println("With valid fluid item");
	            		
	            		FluidStack containerFluidStack = getFluidStack(parContainerStack);
	            		int amountRoomInContainer = CAPACITY - containerFluidStack.amount;
	            		if (amountRoomInContainer <= 0)
	            		{
	            			// DEBUG
	            			System.out.println("No room in container, already full");
	            			
	            			return resultPass;
	            		}
	            		else // room in container
	            		{
	            			// DEBUG
	            			System.out.println("There is room in the container");
	            			
	            			FluidStack sourceFluidStack = getMatchingFluidStack(sourceFluidHandler, ModFluids.SLIME);
	            			if (sourceFluidStack != null)
	            			{
	            				int amountInSource = sourceFluidStack.amount;
	            				if (amountInSource <= 0) // not enough fluid in source
	            				{
	            					// DEBUG
	            					System.out.println("Not enough fluid in source");
	            					
	            					return resultPass;

	            				}
	            				else // some fluid in source
	            				{
	            					// DEBUG
	            					System.out.println("There is some fluid in source");
	    							System.out.println("Before transfer source fluid stack = "+sourceFluidStack.getFluid()+" "+sourceFluidStack.amount+" and container fluid stack now = "+containerFluidStack.getFluid()+" "+containerFluidStack.amount);
	            					
	    							// check whether enough to fill container with some to spare
	    							if (sourceFluidStack.amount > amountRoomInContainer) // some to spare	
    								{	
	    								containerFluidStack.amount = CAPACITY;
	    								sourceFluidStack.amount -= amountRoomInContainer;
	    							}
	    							else // no extra in source after filling containder
	    							{
	    								containerFluidStack.amount = sourceFluidStack.amount;
	    								sourceFluidStack.amount = 0; // used all source amount
	    								parWorld.setBlockToAir(blockPos);
	    							}
	            					
	    	                        SoundEvent soundevent = containerFluidStack.getFluid().getFillSound(containerFluidStack);
	    							parPlayer.playSound(soundevent, 1f, 1f);
	    							
	    							// update tag data
                	            	updateFluidNBT(parContainerStack, containerFluidStack);
	    					        
	    					        // send packet to update player
                	            	sendUpdatePacketToClient(parPlayer);
	    							
	    							// DEBUG
	    							System.out.println("After transfer source fluid stack = "+sourceFluidStack.getFluid()+" "+sourceFluidStack.amount+" and container fluid stack now = "+containerFluidStack.getFluid()+" "+containerFluidStack.amount);
	            					
	            					return ActionResult.newResult(EnumActionResult.SUCCESS, containerFluidHandler.getContainer());
	            				}
	            			}
	            			else // could not find fluid in block that matches itemstack
	            			{
	            				// DEBUG
	            				System.out.println("No matching fluid in block");
	            				
	            				return resultPass;
	            			}
	            		}
	            	}
	            	else // not a proper fluid item
	            	{
		            	// DEBUG
		            	System.out.println("Malformed fluid item at position "+parContainerStack);
		            	
		            	return resultFail;
	            	}
	            }
	            else // not a proper fluid block
	            {
	            	// DEBUG
	            	System.out.println("Malformed fluid block at position = "+blockPos);
	            	
	            	return resultFail;
	            }
	        }
	        else
	        {
	        	// DEBUG
	        	System.out.println("Not a fluid block in that location");
	        	
	        	return resultPass;
	        }
    	}
    }
 }
