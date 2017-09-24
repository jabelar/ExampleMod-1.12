package com.blogspot.jabelarminecraft.examplemod.blocks.fluids;

import com.blogspot.jabelarminecraft.examplemod.init.ModFluids;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

public class FluidHandlerSlimeBag extends FluidHandlerItemStack
{
	private static final int CAPACITY = Fluid.BUCKET_VOLUME;
	
	public FluidHandlerSlimeBag(ItemStack container) 
	{
		super(container, CAPACITY);
		setFluid(new FluidStack(ModFluids.SLIME, CAPACITY));

//		// DEBUG
//		System.out.println("Constructing FluidHandlerSlimeBag with FluidStack = "+getFluid()+" capacity = "+capacity+" and container = "+container);
	}
	
    @Override
	protected void setContainerToEmpty()
    {
        container.getTagCompound().removeTag(FLUID_NBT_KEY);
    }

    @Override
	public boolean canFillFluidType(FluidStack fluid)
    {
        return (fluid.getFluid() == ModFluids.SLIME);
    }
}
