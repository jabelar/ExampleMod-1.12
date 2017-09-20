package com.blogspot.jabelarminecraft.examplemod.init;

import com.blogspot.jabelarminecraft.examplemod.MainMod;
import com.blogspot.jabelarminecraft.examplemod.fluids.ModFluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class ModFluids 
{
	public static final Fluid SLIME = new ModFluid(
			"slime", 
			new ResourceLocation(MainMod.MODID,"slime_still"), 
			new ResourceLocation(MainMod.MODID, "slime_flow"))
			.setDensity(1100)
			.setGaseous(false)
			.setLuminosity(3)
			.setViscosity(1300)
			.setTemperature(300);
}