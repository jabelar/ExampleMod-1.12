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

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;

// TODO: Auto-generated Javadoc
public class BiomeDecoratorCloud extends BiomeDecorator
{
    
    /* (non-Javadoc)
     * @see net.minecraft.world.biome.BiomeDecorator#decorate(net.minecraft.world.World, java.util.Random, net.minecraft.world.biome.Biome, net.minecraft.util.math.BlockPos)
     */
    @Override
    public void decorate(World worldIn, Random random, Biome biome, BlockPos pos)
    {
    }

    /* (non-Javadoc)
     * @see net.minecraft.world.biome.BiomeDecorator#genDecorations(net.minecraft.world.biome.Biome, net.minecraft.world.World, java.util.Random)
     */
    @Override
    protected void genDecorations(Biome biomeIn, World worldIn, Random random)
    {
    }
}
