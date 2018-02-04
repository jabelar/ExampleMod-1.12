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

import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;

// TODO: Auto-generated Javadoc
public class BiomeDecoratorCloud extends BiomeDecorator
{ 
    private int dirtSize = 33;
    private int gravelSize = 33;
    private int graniteSize = 33;
    private int dioriteSize = 33;
    private int andesiteSize = 33;
    private int coalSize = 17;
    private int ironSize = 9;
    private int goldSize = 9;
    private int redstoneSize = 8;
    private int diamondSize = 8;
    private int lapisSize = 8;

    /** 
     * This is the function where ore generation and things like flowers are generated.
     */
    @Override
    public void decorate(World worldIn, Random random, Biome biome, BlockPos pos)
    {
        if (decorating)
        {
            throw new RuntimeException("Already decorating");
        }
        else
        {
            chunkProviderSettings = ChunkGeneratorSettings.Factory.jsonToFactory(worldIn.getWorldInfo().getGeneratorOptions()).build();
            chunkPos = pos;
            
            dirtGen = new WorldGenMinable(Blocks.DIRT.getDefaultState(), dirtSize);
            gravelOreGen = new WorldGenMinable(Blocks.GRAVEL.getDefaultState(), gravelSize);
            graniteGen = new WorldGenMinable(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), graniteSize);
            dioriteGen = new WorldGenMinable(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE), dioriteSize);
            andesiteGen = new WorldGenMinable(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE), andesiteSize);
            coalGen = new WorldGenMinable(Blocks.COAL_ORE.getDefaultState(), coalSize);
            ironGen = new WorldGenMinable(Blocks.IRON_ORE.getDefaultState(), ironSize);
            goldGen = new WorldGenMinable(Blocks.GOLD_ORE.getDefaultState(), goldSize);
            redstoneGen = new WorldGenMinable(Blocks.REDSTONE_ORE.getDefaultState(), redstoneSize);
            diamondGen = new WorldGenMinable(Blocks.DIAMOND_ORE.getDefaultState(), diamondSize);
            lapisGen = new WorldGenMinable(Blocks.LAPIS_ORE.getDefaultState(), lapisSize);
            genDecorations(biome, worldIn, random);
            decorating = false;
        }
    }
    /**
     * This is where things like trees are generated.
    */
    @Override
    protected void genDecorations(Biome biomeIn, World worldIn, Random random)
    {
        super.genDecorations(biomeIn, worldIn, random);
    }
    
    /**
     * Generates ores in the current chunk
     */
    @Override
    protected void generateOres(World worldIn, Random random)
    {
        super.generateOres(worldIn, random);
    }

    /**
     * Standard ore generation helper. Vanilla uses this to generate most ores.
     * The main difference between this and {@link #genStandardOre2} is that this takes min and max heights, while
     * genStandardOre2 takes center and spread.
     */
    @Override
    protected void genStandardOre1(World worldIn, Random random, int blockCount, WorldGenerator generator, int minHeight, int maxHeight)
    {
        super.genStandardOre1(worldIn, random, blockCount, generator, minHeight, maxHeight);
    }

    /**
     * Standard ore generation helper. Vanilla uses this to generate Lapis Lazuli.
     * The main difference between this and {@link #genStandardOre1} is that this takes takes center and spread, while
     * genStandardOre1 takes min and max heights.
     */
    @Override
    protected void genStandardOre2(World worldIn, Random random, int blockCount, WorldGenerator generator, int centerHeight, int spread)
    {
        super.genStandardOre2(worldIn, random, blockCount, generator, centerHeight, spread);
    }
}
