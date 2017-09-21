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
package com.blogspot.jabelarminecraft.examplemod.init;

import com.blogspot.jabelarminecraft.examplemod.MainMod;
import com.blogspot.jabelarminecraft.examplemod.fluids.ModFluid;

import net.minecraft.util.ResourceLocation;
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
			.setViscosity(25000)
			.setTemperature(300);
}