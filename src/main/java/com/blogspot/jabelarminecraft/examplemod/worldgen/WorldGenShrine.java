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

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenShrine implements IWorldGenerator
{

	/* (non-Javadoc)
	 * @see net.minecraftforge.fml.common.IWorldGenerator#generate(java.util.Random, int, int, net.minecraft.world.World, net.minecraft.world.gen.IChunkGenerator, net.minecraft.world.chunk.IChunkProvider)
	 */
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) 
	{
		// DEBUG
		System.out.println("Calling custom world generator");
		// It is important to convert the passed in chunk coordinates to
		// world coordinates.
		int blockX = chunkX * 16;
		int blockZ = chunkZ * 16;
		// generate differently based on dimension
		switch(world.provider.getDimensionType().getId())
		{
		case -1: generateNether(world, random, blockX, blockZ);
		break;
		case 0: generateOverworld(world, random, blockX, blockZ);
		break;
		case 1: generateEnd(world, random, blockX, blockZ);
		break;
		}

	}

	private void generateOverworld(World world, Random random, int blockX, int blockZ) 
	{
		// TODO Auto-generated method stub
		
	}

	private void generateEnd(World world, Random random, int blockX, int blockZ) 
	{
		// TODO Auto-generated method stub
		
	}

	private void generateNether(World world, Random random, int blockX, int blockZ) 
	{
		// TODO Auto-generated method stub
		
	}
}
