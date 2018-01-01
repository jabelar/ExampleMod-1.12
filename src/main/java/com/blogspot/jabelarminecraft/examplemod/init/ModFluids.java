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

import java.util.Set;

import com.blogspot.jabelarminecraft.examplemod.MainMod;
import com.blogspot.jabelarminecraft.examplemod.fluids.ModFluid;
import com.google.common.collect.ImmutableSet;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;

public class ModFluids
{
    /*
     * fluids
     */
    static
    {
        FluidRegistry.enableUniversalBucket();
    }

    public static final ModFluid SLIME = (ModFluid) new ModFluid(
            "slime",
            new ResourceLocation(MainMod.MODID, "slime_still"),
            new ResourceLocation(MainMod.MODID, "slime_flow"))
                    .setHasBucket(true)
                    .setDensity(1100)
                    .setGaseous(false)
                    .setLuminosity(9)
                    .setViscosity(25000)
                    .setTemperature(300);
    
    public static final Set<ModFluid> SET_FLUIDS = ImmutableSet.of(
            SLIME);
    
    /**
     * 
     * Registers fluids.
     */
    public static void registerFluids()
    {
        // DEBUG
        System.out.println("Registering fluids");
        for (final ModFluid fluid : SET_FLUIDS)
        {
            FluidRegistry.registerFluid(fluid);
            if (fluid.isBucketEnabled())
            {
                FluidRegistry.addBucketForFluid(fluid);
            }
            // DEBUG
            System.out.println("Registering fluid: " + fluid.getName()+" with bucket = "+fluid.isBucketEnabled());
        }
    }
}