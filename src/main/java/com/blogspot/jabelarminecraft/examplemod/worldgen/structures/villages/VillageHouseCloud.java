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
package com.blogspot.jabelarminecraft.examplemod.worldgen.structures.villages;

import java.util.Random;

import com.blogspot.jabelarminecraft.examplemod.init.ModBlocks;
import com.blogspot.jabelarminecraft.examplemod.init.ModProfessions;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

// TODO: Auto-generated Javadoc
public class VillageHouseCloud extends StructureVillagePieces.Village
{
    
    /**
     * Instantiates a new village house cloud.
     */
    public VillageHouseCloud()
    {
    }

    /**
     * Instantiates a new village house cloud.
     *
     * @param parStart the par start
     * @param parType the par type
     * @param parRand the par rand
     * @param parStructBB the par struct BB
     * @param parFacing the par facing
     */
    public VillageHouseCloud(StructureVillagePieces.Start parStart, int parType, Random parRand, StructureBoundingBox parStructBB, EnumFacing parFacing)
    {
        super(parStart, parType);
        setCoordBaseMode(parFacing);
        boundingBox = parStructBB;
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
     * Mineshafts at the end, it adds Fences...
     *
     * @param parWorld the par world
     * @param parRand the par rand
     * @param parStructBB the par struct BB
     * @return true, if successful
     */
    @Override
    public boolean addComponentParts(World parWorld, Random parRand, StructureBoundingBox parStructBB)
    {
        if (averageGroundLvl < 0)
        {
            averageGroundLvl = getAverageGroundLevel(parWorld, parStructBB);

            if (averageGroundLvl < 0)
            {
                return true;
            }

            boundingBox.offset(0, averageGroundLvl - boundingBox.maxY + 7 - 1, 0);
        }

        IBlockState wallBlockState = getBiomeSpecificBlockState(ModBlocks.cloud.getDefaultState());
        IBlockState stateStairsNorth = getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
        IBlockState stateStairsSouth = getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH));
        IBlockState stateStairsEast = getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST));
        IBlockState stateStairsWest = getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST));
        IBlockState statePlanks = getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState());
        IBlockState stateLog = getBiomeSpecificBlockState(ModBlocks.cloud_log.getDefaultState());
        fillWithBlocks(parWorld, parStructBB, 1, 1, 1, 7, 4, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        fillWithBlocks(parWorld, parStructBB, 2, 1, 6, 8, 4, 10, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        fillWithBlocks(parWorld, parStructBB, 2, 0, 5, 8, 0, 10, statePlanks, statePlanks, false);
        fillWithBlocks(parWorld, parStructBB, 1, 0, 1, 7, 0, 4, statePlanks, statePlanks, false);
        fillWithBlocks(parWorld, parStructBB, 0, 0, 0, 0, 3, 5, wallBlockState, wallBlockState, false);
        fillWithBlocks(parWorld, parStructBB, 8, 0, 0, 8, 3, 10, wallBlockState, wallBlockState, false);
        fillWithBlocks(parWorld, parStructBB, 1, 0, 0, 7, 2, 0, wallBlockState, wallBlockState, false);
        fillWithBlocks(parWorld, parStructBB, 1, 0, 5, 2, 1, 5, wallBlockState, wallBlockState, false);
        fillWithBlocks(parWorld, parStructBB, 2, 0, 6, 2, 3, 10, wallBlockState, wallBlockState, false);
        fillWithBlocks(parWorld, parStructBB, 3, 0, 10, 7, 3, 10, wallBlockState, wallBlockState, false);
        fillWithBlocks(parWorld, parStructBB, 1, 2, 0, 7, 3, 0, statePlanks, statePlanks, false);
        fillWithBlocks(parWorld, parStructBB, 1, 2, 5, 2, 3, 5, statePlanks, statePlanks, false);
        fillWithBlocks(parWorld, parStructBB, 0, 4, 1, 8, 4, 1, statePlanks, statePlanks, false);
        fillWithBlocks(parWorld, parStructBB, 0, 4, 4, 3, 4, 4, statePlanks, statePlanks, false);
        fillWithBlocks(parWorld, parStructBB, 0, 5, 2, 8, 5, 3, statePlanks, statePlanks, false);
        setBlockState(parWorld, statePlanks, 0, 4, 2, parStructBB);
        setBlockState(parWorld, statePlanks, 0, 4, 3, parStructBB);
        setBlockState(parWorld, statePlanks, 8, 4, 2, parStructBB);
        setBlockState(parWorld, statePlanks, 8, 4, 3, parStructBB);
        setBlockState(parWorld, statePlanks, 8, 4, 4, parStructBB);

        for (int i = -1; i <= 2; ++i)
        {
            for (int j = 0; j <= 8; ++j)
            {
                setBlockState(parWorld, stateStairsNorth, j, 4 + i, i, parStructBB);

                if ((i > -1 || j <= 1) && (i > 0 || j <= 3) && (i > 1 || j <= 4 || j >= 6))
                {
                    setBlockState(parWorld, stateStairsSouth, j, 4 + i, 5 - i, parStructBB);
                }
            }
        }

        fillWithBlocks(parWorld, parStructBB, 3, 4, 5, 3, 4, 10, statePlanks, statePlanks, false);
        fillWithBlocks(parWorld, parStructBB, 7, 4, 2, 7, 4, 10, statePlanks, statePlanks, false);
        fillWithBlocks(parWorld, parStructBB, 4, 5, 4, 4, 5, 10, statePlanks, statePlanks, false);
        fillWithBlocks(parWorld, parStructBB, 6, 5, 4, 6, 5, 10, statePlanks, statePlanks, false);
        fillWithBlocks(parWorld, parStructBB, 5, 6, 3, 5, 6, 10, statePlanks, statePlanks, false);

        for (int k = 4; k >= 1; --k)
        {
            setBlockState(parWorld, statePlanks, k, 2 + k, 7 - k, parStructBB);

            for (int k1 = 8 - k; k1 <= 10; ++k1)
            {
                setBlockState(parWorld, stateStairsEast, k, 2 + k, k1, parStructBB);
            }
        }

        setBlockState(parWorld, statePlanks, 6, 6, 3, parStructBB);
        setBlockState(parWorld, statePlanks, 7, 5, 4, parStructBB);
        setBlockState(parWorld, stateStairsWest, 6, 6, 4, parStructBB);

        for (int l = 6; l <= 8; ++l)
        {
            for (int l1 = 5; l1 <= 10; ++l1)
            {
                setBlockState(parWorld, stateStairsWest, l, 12 - l, l1, parStructBB);
            }
        }

        setBlockState(parWorld, stateLog, 0, 2, 1, parStructBB);
        setBlockState(parWorld, stateLog, 0, 2, 4, parStructBB);
        setBlockState(parWorld, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, parStructBB);
        setBlockState(parWorld, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 3, parStructBB);
        setBlockState(parWorld, stateLog, 4, 2, 0, parStructBB);
        setBlockState(parWorld, Blocks.GLASS_PANE.getDefaultState(), 5, 2, 0, parStructBB);
        setBlockState(parWorld, stateLog, 6, 2, 0, parStructBB);
        setBlockState(parWorld, stateLog, 8, 2, 1, parStructBB);
        setBlockState(parWorld, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 2, parStructBB);
        setBlockState(parWorld, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 3, parStructBB);
        setBlockState(parWorld, stateLog, 8, 2, 4, parStructBB);
        setBlockState(parWorld, statePlanks, 8, 2, 5, parStructBB);
        setBlockState(parWorld, stateLog, 8, 2, 6, parStructBB);
        setBlockState(parWorld, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 7, parStructBB);
        setBlockState(parWorld, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 8, parStructBB);
        setBlockState(parWorld, stateLog, 8, 2, 9, parStructBB);
        setBlockState(parWorld, stateLog, 2, 2, 6, parStructBB);
        setBlockState(parWorld, Blocks.GLASS_PANE.getDefaultState(), 2, 2, 7, parStructBB);
        setBlockState(parWorld, Blocks.GLASS_PANE.getDefaultState(), 2, 2, 8, parStructBB);
        setBlockState(parWorld, stateLog, 2, 2, 9, parStructBB);
        setBlockState(parWorld, stateLog, 4, 4, 10, parStructBB);
        setBlockState(parWorld, Blocks.GLASS_PANE.getDefaultState(), 5, 4, 10, parStructBB);
        setBlockState(parWorld, stateLog, 6, 4, 10, parStructBB);
        setBlockState(parWorld, statePlanks, 5, 5, 10, parStructBB);
        setBlockState(parWorld, Blocks.AIR.getDefaultState(), 2, 1, 0, parStructBB);
        setBlockState(parWorld, Blocks.AIR.getDefaultState(), 2, 2, 0, parStructBB);
        placeTorch(parWorld, EnumFacing.NORTH, 2, 3, 1, parStructBB);
        createVillageDoor(parWorld, parStructBB, parRand, 2, 1, 0, EnumFacing.NORTH);
        fillWithBlocks(parWorld, parStructBB, 1, 0, -1, 3, 2, -1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);

        if (getBlockStateFromPos(parWorld, 2, 0, -1, parStructBB).getMaterial() == Material.AIR && getBlockStateFromPos(parWorld, 2, -1, -1, parStructBB).getMaterial() != Material.AIR)
        {
            setBlockState(parWorld, stateStairsNorth, 2, 0, -1, parStructBB);

            if (getBlockStateFromPos(parWorld, 2, -1, -1, parStructBB).getBlock() == Blocks.GRASS_PATH)
            {
                setBlockState(parWorld, Blocks.GRASS.getDefaultState(), 2, -1, -1, parStructBB);
            }
        }

        for (int i1 = 0; i1 < 5; ++i1)
        {
            for (int i2 = 0; i2 < 9; ++i2)
            {
                clearCurrentPositionBlocksUpwards(parWorld, i2, 7, i1, parStructBB);
                replaceAirAndLiquidDownwards(parWorld, wallBlockState, i2, -1, i1, parStructBB);
            }
        }

        for (int j1 = 5; j1 < 11; ++j1)
        {
            for (int j2 = 2; j2 < 9; ++j2)
            {
                clearCurrentPositionBlocksUpwards(parWorld, j2, 7, j1, parStructBB);
                replaceAirAndLiquidDownwards(parWorld, wallBlockState, j2, -1, j1, parStructBB);
            }
        }

        spawnVillagers(parWorld, parStructBB, 4, 1, 2, 2);
        return true;
    }
    
    /* (non-Javadoc)
     * @see net.minecraft.world.gen.structure.StructureVillagePieces.Village#getBiomeSpecificBlockState(net.minecraft.block.state.IBlockState)
     */
    @Override
    protected IBlockState getBiomeSpecificBlockState(IBlockState blockstateIn)
    {
        net.minecraftforge.event.terraingen.BiomeEvent.GetVillageBlockID event = new net.minecraftforge.event.terraingen.BiomeEvent.GetVillageBlockID(startPiece == null ? null : startPiece.biome, blockstateIn);
        net.minecraftforge.common.MinecraftForge.TERRAIN_GEN_BUS.post(event);
        if (event.getResult() == net.minecraftforge.fml.common.eventhandler.Event.Result.DENY) return event.getReplacement();
        
        /*
         * You can check the biome here using startPiece.biome and modify your 
         * structure accordingly.
         */
        return blockstateIn;
    }

    /* (non-Javadoc)
     * @see net.minecraft.world.gen.structure.StructureVillagePieces.Village#biomeDoor()
     */
    @Override
    protected BlockDoor biomeDoor()
    {
        
        /*
         * You can check the biome here using startPiece.biome and modify your 
         * structure accordingly.
         */
         return Blocks.OAK_DOOR;
    }
    
    /* (non-Javadoc)
     * @see net.minecraft.world.gen.structure.StructureVillagePieces.Village#chooseForgeProfession(int, net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession)
     */
    @Override
    protected VillagerRegistry.VillagerProfession chooseForgeProfession(int count, VillagerRegistry.VillagerProfession prof)
    {
        return ModProfessions.mysterious_stranger;
    }
}
