package com.blogspot.jabelarminecraft.examplemod.init;

import com.blogspot.jabelarminecraft.examplemod.MainMod;
import com.blogspot.jabelarminecraft.examplemod.fluids.ModFluid;
import com.blogspot.jabelarminecraft.examplemod.utilities.Utilities;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class ModFluids 
{
	/*
	 * fluids
	 */
	public static final Fluid SLIME = new ModFluid(
			"slime", 
			new ResourceLocation(MainMod.MODID,"slime_still"), 
			new ResourceLocation(MainMod.MODID, "slime_flow"))
			.setDensity(1100)
			.setGaseous(false)
			.setLuminosity(3)
			.setViscosity(1300)
			.setTemperature(300);
	
	/*
	 * fluid blocks
	 * Make sure you set registry name here
	 */
	public static final BlockFluidBase SLIME_BLOCK = (BlockFluidBase) Utilities.setBlockName(new BlockFluidClassic(SLIME, ModMaterials.SLIME), "slime");
}