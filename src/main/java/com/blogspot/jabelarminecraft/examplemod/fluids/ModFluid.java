package com.blogspot.jabelarminecraft.examplemod.fluids;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class ModFluid extends Fluid
{
	protected static int color = 0xFFFFFFFF;
	
	public ModFluid(String fluidName, ResourceLocation still, ResourceLocation flowing) {
		super(fluidName, still, flowing);
	}

	@Override
	public int getColor()
	{
		return color;
	}
	
	public Fluid setColor(int parColor)
	{
		color = parColor;
		return this;
	}
}
