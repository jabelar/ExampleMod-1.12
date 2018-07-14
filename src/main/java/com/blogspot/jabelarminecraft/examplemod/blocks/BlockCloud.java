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

import net.minecraft.block.BlockIce;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// TODO: Auto-generated Javadoc
/**
 * @author jabelar
 *
 */
public class BlockCloud extends BlockIce
{

    /**
     * Instantiates a new block cloud.
     */
    @SuppressWarnings("deprecation")
    public BlockCloud()
    {
        super();
        
        // DEBUG
        System.out.println("BlockCloud constructor");
        
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        blockSoundType = SoundType.SNOW;
        blockParticleGravity = 1.0F;
        slipperiness = 0.6F;
        lightOpacity = 20; 
        setTickRandomly(false);
        setLightLevel(0.5F); // redstone light has light value of 1.0F
    }

    /* (non-Javadoc)
     * @see net.minecraft.block.BlockIce#getBlockLayer()
     */
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }
    
    /* (non-Javadoc)
     * @see net.minecraft.block.Block#canCreatureSpawn(net.minecraft.block.state.IBlockState, net.minecraft.world.IBlockAccess, net.minecraft.util.math.BlockPos, net.minecraft.entity.EntityLiving.SpawnPlacementType)
     */
    @Override
    public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, net.minecraft.entity.EntityLiving.SpawnPlacementType type)
    {
        return true;
    }
    

    /* (non-Javadoc)
     * @see net.minecraft.block.Block#hasCustomBreakingProgress(net.minecraft.block.state.IBlockState)
     */
    @Override
    @Deprecated
    @SideOnly(Side.CLIENT)
    public boolean hasCustomBreakingProgress(IBlockState state)
    {
        return false;
    }
    
    /* (non-Javadoc)
     * @see net.minecraft.block.Block#canSustainPlant(net.minecraft.block.state.IBlockState, net.minecraft.world.IBlockAccess, net.minecraft.util.math.BlockPos, net.minecraft.util.EnumFacing, net.minecraftforge.common.IPlantable)
     */
    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, net.minecraftforge.common.IPlantable plantable)
    {
        // TO-DO
        // Should make this more specific to various types of plants
        return true;
    }
    
    /* (non-Javadoc)
     * @see net.minecraft.block.Block#onPlantGrow(net.minecraft.block.state.IBlockState, net.minecraft.world.World, net.minecraft.util.math.BlockPos, net.minecraft.util.math.BlockPos)
     */
    @Override
    public void onPlantGrow(IBlockState state, World world, BlockPos pos, BlockPos source)
    {
        // Some trees convert the soil under the trunk
    }
}
