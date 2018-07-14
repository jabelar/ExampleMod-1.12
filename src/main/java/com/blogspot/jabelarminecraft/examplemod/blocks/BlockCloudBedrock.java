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
package com.blogspot.jabelarminecraft.examplemod.blocks;

import com.blogspot.jabelarminecraft.examplemod.init.ModMaterials;

import net.minecraft.block.BlockEmptyDrops;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// TODO: Auto-generated Javadoc
public class BlockCloudBedrock extends BlockEmptyDrops
{

    /**
     * Instantiates a new block cloud bedrock.
     */
    @SuppressWarnings("deprecation")
    public BlockCloudBedrock()
    {
        super(ModMaterials.CLOUD_ROCK);

        // DEBUG
        System.out.println("BlockCloud constructor");

        setBlockUnbreakable();
        setResistance(6000000.0F);
        disableStats();
        blockSoundType = SoundType.SNOW;
        blockParticleGravity = 1.0F;
        slipperiness = 0.6F;
        lightOpacity = 20; // cast a light shadow
        setTickRandomly(false);
        setLightLevel(0.5F); // redstone light has light value of 1.0F
        useNeighborBrightness = false;
    }

    /* (non-Javadoc)
     * @see net.minecraft.block.Block#getBlockLayer()
     */
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.SOLID;
    }
    
    /* (non-Javadoc)
     * @see net.minecraft.block.Block#canCreatureSpawn(net.minecraft.block.state.IBlockState, net.minecraft.world.IBlockAccess, net.minecraft.util.math.BlockPos, net.minecraft.entity.EntityLiving.SpawnPlacementType)
     */
    @Override
    public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, net.minecraft.entity.EntityLiving.SpawnPlacementType type)
    {
        return false;
    }
}