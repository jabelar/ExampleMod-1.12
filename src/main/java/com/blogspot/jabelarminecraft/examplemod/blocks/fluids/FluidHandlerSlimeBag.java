package com.blogspot.jabelarminecraft.examplemod.blocks.fluids;

import com.blogspot.jabelarminecraft.examplemod.init.ModFluids;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

public class FluidHandlerSlimeBag extends FluidHandlerItemStack
{
	public FluidHandlerSlimeBag(ItemStack parContainerStack, int parCapacity) 
	{
		super(parContainerStack, parCapacity);
		setFluid(new FluidStack(ModFluids.SLIME, 0)); // start empty

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
