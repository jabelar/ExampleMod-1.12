package com.blogspot.jabelarminecraft.examplemod.items.fluidcontainers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.blogspot.jabelarminecraft.examplemod.blocks.fluids.FluidHandlerSlimeBag;
import com.blogspot.jabelarminecraft.examplemod.utilities.Utilities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.ItemFluidContainer;
import net.minecraftforge.items.ItemHandlerHelper;

public class ItemSlimeBag extends ItemFluidContainer
{
	public ItemSlimeBag() 
	{
		super(Fluid.BUCKET_VOLUME);
		Utilities.setItemName(this, "slime_bag");
		setCreativeTab(CreativeTabs.MISC);
		
		// DEBUG
		System.out.println("Constructing ItemSlimeBag");
	}
	
    @Override
    public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable NBTTagCompound nbt)
    {
//    	// DEBUG
//    	System.out.println("initCapabilities for ItemSlimeBag");
    	
        return new FluidHandlerSlimeBag(stack);
    }
    
    /**
     * Called when the equipped item is right clicked.
     */
    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World parWorld, @Nonnull EntityPlayer parPlayer, @Nonnull EnumHand parHand)
    {
    	// DEBUG
    	System.out.println("onItemRightClick for ItemSlimeBag");
    
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
                if (fluidStack == null)
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
    
    public ActionResult<ItemStack> tryFill(World parWorld, EntityPlayer parPlayer, RayTraceResult parRayTraceTarget, ItemStack parStack)
    {
    	BlockPos pos = parRayTraceTarget.getBlockPos();
        ItemStack resultStack = parStack.copy();
        resultStack.setCount(1);

        FluidActionResult filledResult = tryPickUpFluid(resultStack, parPlayer, parWorld, pos, parRayTraceTarget.sideHit);
        if (filledResult.isSuccess())
        {
        	// DEBUG
        	System.out.println("Successful at picking up fluid item stack = "+filledResult.getResult());
        	
            ItemHandlerHelper.giveItemToPlayer(parPlayer, filledResult.getResult());
            return ActionResult.newResult(EnumActionResult.SUCCESS, filledResult.getResult());
        }
        else
        {
            // DEBUG
        	System.out.println("Not successful at picking up fluid");
            return ActionResult.newResult(EnumActionResult.FAIL, filledResult.getResult());
        }
    }
    
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
	            ItemStack drained = result.getResult();
	            ItemStack emptyStack = !drained.isEmpty() ? drained.copy() : new ItemStack(this);
	
	            // DEBUG
	        	System.out.println("Adding empty slime bag to player inventory = "+emptyStack);
	        	
	            // add empty bucket to player inventory
	            ItemHandlerHelper.giveItemToPlayer(parPlayer, emptyStack);
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
    
    @Nonnull
    public static FluidActionResult tryPickUpFluid(@Nonnull ItemStack emptyContainer, @Nullable EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side)
    {
        if (emptyContainer.isEmpty() || worldIn == null || pos == null)
        {
        	// DEBUG
        	System.out.println("Container is empty or world or positions are null");
            return FluidActionResult.FAILURE;
        }

        IBlockState state = worldIn.getBlockState(pos);
        Block block = state.getBlock();

        if (block instanceof IFluidBlock || block instanceof BlockLiquid)
        {
        	// DEBUG
        	System.out.println("Block is an instance of IFluidBlock or BlockLiquid");
        	
            IFluidHandler targetFluidHandler = FluidUtil.getFluidHandler(worldIn, pos, side);
            if (targetFluidHandler != null)
            {
            	// DEBUG
            	System.out.println("target has fluid handler");
            	
                return FluidUtil.tryFillContainer(emptyContainer, targetFluidHandler, Integer.MAX_VALUE, playerIn, true);
            }
            else
            {
            	// DEBUG
            	System.out.println("target doesn't have fluid handler");
            }
        }
        else
        {
        	// DEBUG
        	System.out.println("Not an IFluidBlock or BlockLiquid, instead = "+block);
        }
        return FluidActionResult.FAILURE;
    }


    @Nullable
	public FluidStack getFluidStack(final ItemStack container) 
    {
		return FluidUtil.getFluidContained(container);
	}
 }
