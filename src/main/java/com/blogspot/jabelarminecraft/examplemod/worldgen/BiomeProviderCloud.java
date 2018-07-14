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
package com.blogspot.jabelarminecraft.examplemod.worldgen;

import com.blogspot.jabelarminecraft.examplemod.init.ModBiomes;

import net.minecraft.world.biome.BiomeProviderSingle;

public class BiomeProviderCloud extends BiomeProviderSingle
{
    
    /**
     * Instantiates a new biome provider cloud.
     */
    public BiomeProviderCloud()
    {
        super(ModBiomes.cloud);
        
        // DEBUG
        System.out.println("Constructing BiomeProviderCloud");
    }
}
