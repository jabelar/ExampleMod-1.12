package com.blogspot.jabelarminecraft.examplemod.items.fluidcontainers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.blogspot.jabelarminecraft.examplemod.blocks.fluids.FluidHandlerSlimeBag;
import com.blogspot.jabelarminecraft.examplemod.utilities.Utilities;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
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
    	// DEBUG
    	System.out.println("initCapabilities for ItemSlimeBag");
    	
        return new FluidHandlerSlimeBag(stack);
    }
    
    /**
     * Called when the equipped item is right clicked.
     */
    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull EnumHand hand)
    {
    	// DEBUG
    	System.out.println("onItemRightClick for ItemSlimeBag");
    
        ItemStack itemstack = player.getHeldItem(hand);
        
        // DEBUG
        System.out.println("finding FluidStack from ItemStack NBT");
        
        FluidStack fluidStack = getFluid(itemstack);
        // empty bucket shouldn't exist, do nothing since it should be handled by the bucket event
        if (fluidStack == null)
        {
        	// DEBUG
        	System.out.println("Can't use item because fluid stack is null");
        	
            return ActionResult.newResult(EnumActionResult.PASS, itemstack);
        }

        // clicked on a block?
        RayTraceResult mop = this.rayTrace(world, player, false);

        if(mop == null || mop.typeOfHit != RayTraceResult.Type.BLOCK)
        {
            return ActionResult.newResult(EnumActionResult.PASS, itemstack);
        }

        // DEBUG
        System.out.println("Slime bag used");
        
        BlockPos clickPos = mop.getBlockPos();
        // can we place liquid there?
        if (world.isBlockModifiable(player, clickPos))
        {
            // the block adjacent to the side we clicked on
            BlockPos targetPos = clickPos.offset(mop.sideHit);

            // can the player place there?
            if (player.canPlayerEdit(targetPos, mop.sideHit, itemstack))
            {
                // try placing liquid
                FluidActionResult result = FluidUtil.tryPlaceFluid(player, world, targetPos, itemstack, fluidStack);
                
                // DEBUG
                System.out.println("Tried to place fluid with result = "+result);
                
                if (result.isSuccess() && !player.capabilities.isCreativeMode)
                {
                    // success!
                    player.addStat(StatList.getObjectUseStats(this));

                    itemstack.shrink(1);
                    ItemStack drained = result.getResult();
                    ItemStack emptyStack = !drained.isEmpty() ? drained.copy() : new ItemStack(this);

                    // check whether we replace the item or add the empty one to the inventory
                    if (itemstack.isEmpty())
                    {
                        return ActionResult.newResult(EnumActionResult.SUCCESS, emptyStack);
                    }
                    else
                    {
                        // add empty bucket to player inventory
                        ItemHandlerHelper.giveItemToPlayer(player, emptyStack);
                        return ActionResult.newResult(EnumActionResult.SUCCESS, itemstack);
                    }
                }
            }
        }
        
        // DEBUG
        System.out.println("Failed to place fluid");

        // couldn't place liquid there
        return ActionResult.newResult(EnumActionResult.FAIL, itemstack);
    }
    
    @Nullable
	public FluidStack getFluid(final ItemStack container) {
		return FluidUtil.getFluidContained(container);
	}
 }
