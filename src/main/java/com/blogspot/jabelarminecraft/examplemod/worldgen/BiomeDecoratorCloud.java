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

import com.blogspot.jabelarminecraft.examplemod.init.ModBlocks;
import com.google.common.base.Predicate;

import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

// TODO: Auto-generated Javadoc
public class BiomeDecoratorCloud extends BiomeDecorator
{ 
    Predicate<IBlockState> replaceablePredicate = new CloudPredicate();
    
    // If you want to make these configurable, you'll need a ChunkGeneratorSettings
    // instance and use the fields from there instead.
    private int dirtSize = 33;
    private int gravelSize = 33;
    private int graniteSize = 33;
    private int dioriteSize = 33;
    private int andesiteSize = 33;
    private int coalSize = 17;
    private int ironSize = 9;
    private int goldSize = 9;
    private int redstoneSize = 9;
    private int diamondSize = 8;
    private int lapisSize = 8;

    private int dirtCount = 10;
    private int gravelCount = 8;
    private int dioriteCount = 10;
    private int graniteCount = 10;
    private int andesiteCount = 10;
    private int coalCount = 20;
    private int ironCount = 20;
    private int goldCount = 2;
    private int redstoneCount = 8;
    private int diamondCount = 1;
    private int lapisCount = 1;
    
    private int lapisCenterHeight =6;
    private int lapisSpread = 16;

    private int oreGenMinHeight = 0;

    private int dirtMaxHeight = 255;
    private int gravelMaxHeight = 255;
    private int dioriteMaxHeight = 80;
    private int graniteMaxHeight = 80;
    private int andesiteMaxHeight = 80;
    private int coalMaxHeight = 126;
    private int ironMaxHeight = 64;
    private int goldMaxHeight = 32;
    private int redstoneMaxHeight = 16;
    private int diamondMaxHeight = 16;

    public BiomeDecoratorCloud()
    {
        super();
        
        // Must use predicate version if you wnat to replace custom blocks, otherwise will
        // only replace Blocks.STONE.
        dirtGen = new WorldGenMinable(Blocks.DIRT.getDefaultState(), dirtSize, replaceablePredicate);
        gravelOreGen = new WorldGenMinable(Blocks.GRAVEL.getDefaultState(), gravelSize, replaceablePredicate);
        graniteGen = new WorldGenMinable(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), graniteSize, replaceablePredicate);
        dioriteGen = new WorldGenMinable(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE), dioriteSize, replaceablePredicate);
        andesiteGen = new WorldGenMinable(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE), andesiteSize, replaceablePredicate);
        coalGen = new WorldGenMinable(Blocks.COAL_ORE.getDefaultState(), coalSize, replaceablePredicate);
        ironGen = new WorldGenMinable(Blocks.IRON_ORE.getDefaultState(), ironSize, replaceablePredicate);
        goldGen = new WorldGenMinable(Blocks.GOLD_ORE.getDefaultState(), goldSize, replaceablePredicate);
        redstoneGen = new WorldGenMinable(Blocks.REDSTONE_ORE.getDefaultState(), redstoneSize, replaceablePredicate);
        diamondGen = new WorldGenMinable(Blocks.DIAMOND_ORE.getDefaultState(), diamondSize, replaceablePredicate);
        lapisGen = new WorldGenMinable(Blocks.LAPIS_ORE.getDefaultState(), lapisSize, replaceablePredicate);
    }

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
            chunkPos = pos;
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
        net.minecraftforge.common.MinecraftForge.ORE_GEN_BUS.post(new OreGenEvent.Pre(worldIn, random, chunkPos));
        if (TerrainGen.generateOre(worldIn, random, dirtGen, chunkPos, OreGenEvent.GenerateMinable.EventType.DIRT))
        this.genStandardOre1(worldIn, random, dirtCount, this.dirtGen, oreGenMinHeight, dirtMaxHeight);
        if (TerrainGen.generateOre(worldIn, random, gravelOreGen, chunkPos, OreGenEvent.GenerateMinable.EventType.GRAVEL))
        this.genStandardOre1(worldIn, random, gravelCount, this.gravelOreGen, oreGenMinHeight, gravelMaxHeight);
        if (TerrainGen.generateOre(worldIn, random, dioriteGen, chunkPos, OreGenEvent.GenerateMinable.EventType.DIORITE))
        this.genStandardOre1(worldIn, random, dioriteCount, this.dioriteGen, oreGenMinHeight, dioriteMaxHeight);
        if (TerrainGen.generateOre(worldIn, random, graniteGen, chunkPos, OreGenEvent.GenerateMinable.EventType.GRANITE))
        this.genStandardOre1(worldIn, random, graniteCount, this.graniteGen, oreGenMinHeight, graniteMaxHeight);
        if (TerrainGen.generateOre(worldIn, random, andesiteGen, chunkPos, OreGenEvent.GenerateMinable.EventType.ANDESITE))
        this.genStandardOre1(worldIn, random, andesiteCount, this.andesiteGen, oreGenMinHeight, andesiteMaxHeight);
        if (TerrainGen.generateOre(worldIn, random, coalGen, chunkPos, OreGenEvent.GenerateMinable.EventType.COAL))
        this.genStandardOre1(worldIn, random, coalCount, this.coalGen, oreGenMinHeight, coalMaxHeight);
        if (TerrainGen.generateOre(worldIn, random, ironGen, chunkPos, OreGenEvent.GenerateMinable.EventType.IRON))
        this.genStandardOre1(worldIn, random, ironCount, this.ironGen, oreGenMinHeight, ironMaxHeight);
        if (TerrainGen.generateOre(worldIn, random, goldGen, chunkPos, OreGenEvent.GenerateMinable.EventType.GOLD))
        this.genStandardOre1(worldIn, random, goldCount, this.goldGen, oreGenMinHeight, goldMaxHeight);
        if (TerrainGen.generateOre(worldIn, random, redstoneGen, chunkPos, OreGenEvent.GenerateMinable.EventType.REDSTONE))
        this.genStandardOre1(worldIn, random, redstoneCount, this.redstoneGen, oreGenMinHeight, redstoneMaxHeight);
        if (TerrainGen.generateOre(worldIn, random, diamondGen, chunkPos, OreGenEvent.GenerateMinable.EventType.DIAMOND))
        this.genStandardOre1(worldIn, random, diamondCount, this.diamondGen, oreGenMinHeight, diamondMaxHeight);
        if (TerrainGen.generateOre(worldIn, random, lapisGen, chunkPos, OreGenEvent.GenerateMinable.EventType.LAPIS))
        this.genStandardOre2(worldIn, random, lapisCount, this.lapisGen, lapisCenterHeight, lapisSpread);
        net.minecraftforge.common.MinecraftForge.ORE_GEN_BUS.post(new OreGenEvent.Post(worldIn, random, chunkPos));
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
    
    static class CloudPredicate implements Predicate<IBlockState>
    {
        private CloudPredicate()
        {
        }

        @Override
        public boolean apply(IBlockState parBlockState)
        {
            if (parBlockState != null && parBlockState.getBlock() == ModBlocks.cloud)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }
}
