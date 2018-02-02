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

import java.util.ArrayList;
import java.util.Random;

import com.blogspot.jabelarminecraft.examplemod.init.ModMaterials;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
public class BlockCloudTop extends Block
{

    /**
     * Instantiates a new block cloud top.
     */
    public BlockCloudTop()
    {
        super(ModMaterials.CLOUD);
        
        // DEBUG
        System.out.println("BlockCloud constructor");
        
        // override default values of Block, where appropriate
//        setUnlocalizedName("magicbeanscloud");
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        blockSoundType = SoundType.SNOW;
        blockParticleGravity = 1.0F;
        slipperiness = 0.6F;
        lightOpacity = 20; // cast a light shadow
        setBlockUnbreakable();
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
        return BlockRenderLayer.TRANSLUCENT;
    }
    
    /* (non-Javadoc)
     * @see net.minecraft.block.Block#doesSideBlockRendering(net.minecraft.block.state.IBlockState, net.minecraft.world.IBlockAccess, net.minecraft.util.math.BlockPos, net.minecraft.util.EnumFacing)
     */
    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
    {
        return false;
    }
    
//    /**
//     * Returns true if the given side of this block type should be rendered (if it's solid or not), if the adjacent
//     * block is at the given coordinates. Args: blockAccess, x, y, z, side
//     */
//    @Override
//    public boolean isBlockSolid(IBlockAccess p_149747_1_, BlockPos parPos, EnumFacing parSide)
//    {
//        return getMaterial(getDefaultState()).isSolid();
//    }

    /**
 * Returns the mobility information of the block, 0 = free, 1 = can't push but can move over, 2 = total immobility
 * and stop pistons.
 *
 * @param parIBlockState the par I block state
 * @return the mobility flag
 */
    @Override
    public EnumPushReaction getMobilityFlag(IBlockState parIBlockState)
    {
        return parIBlockState.getMobilityFlag();
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render.
     *
     * @param state the state
     * @return true, if is opaque cube
     */
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    /**
     * Checks if the block is a solid face on the given side, used by placement logic.
     *
     * @param state the state
     * @param world The current world
     * @param parPos the par pos
     * @param parSide the par side
     * @return True if the block is solid on the specified side.
     */
    @Override
    public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos parPos, EnumFacing parSide)
    {
        return true; 
    }
    
    /* (non-Javadoc)
     * @see net.minecraft.block.Block#isNormalCube(net.minecraft.block.state.IBlockState)
     */
    @Override
    public boolean isNormalCube(IBlockState state)
    {
        return true;
    }
    
    /* (non-Javadoc)
     * @see net.minecraft.block.Block#isNormalCube(net.minecraft.block.state.IBlockState, net.minecraft.world.IBlockAccess, net.minecraft.util.math.BlockPos)
     */
    @Override
    public boolean isNormalCube(IBlockState parState, IBlockAccess parWorld, BlockPos parPos)
    {
        return true;
    }
    
    /* (non-Javadoc)
     * @see net.minecraft.block.Block#shouldSideBeRendered(net.minecraft.block.state.IBlockState, net.minecraft.world.IBlockAccess, net.minecraft.util.math.BlockPos, net.minecraft.util.EnumFacing)
     */
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return true;
    }

    /**
     * Determines if the current block is replaceable by Ore veins during world generation.
     *
     * @param state The current state
     * @param world The current world
     * @param pos Block position in world
     * @param target The generic target block the gen is looking for, Standards define stone
     *      for overworld generation, and neatherack for the nether.
     * @return True to allow this block to be replaced by a ore
     */
    @Override
    public boolean isReplaceableOreGen(IBlockState state, IBlockAccess world, BlockPos pos, com.google.common.base.Predicate<IBlockState> target)
    {
        return target.apply(state);
    }

    /**
     * Chance that fire will spread and consume this block.
     * 300 being a 100% chance, 0, being a 0% chance.
     *
     * @param world The current world
     * @param parPos the par pos
     * @param parSide the par side
     * @return A number ranging from 0 to 300 relating used to determine if the block will be consumed by fire
     */
    @Override
    public int getFlammability(IBlockAccess world, BlockPos parPos, EnumFacing parSide)
    {
        return 0;
    }

    /**
     * Currently only called by fire when it is on top of this block.
     * Returning true will prevent the fire from naturally dying during updating.
     * Also prevents firing from dying from rain.
     *
     * @param parWorld the par world
     * @param parPos the par pos
     * @param parSide the par side
     * @return True if this block sustains fire, meaning it will never go out.
     */
    @Override
    public boolean isFireSource(World parWorld, BlockPos parPos, EnumFacing parSide)
    {
        return false;
    }

    /**
     * Metadata and fortune sensitive version, this replaces the old (int meta, Random rand)
     * version in 1.1.
     *
     * @param parState the par state
     * @param parFortune the par fortune
     * @param parRandom the par random
     * @return The number of items to drop
     */
    @Override
    public int quantityDropped(IBlockState parState, int parFortune, Random parRandom)
    {
        /**
         * Returns the usual quantity dropped by the block plus a bonus of 1 to 'i' (inclusive).
         */
        return 0;
    }

    /**
     * This returns a complete list of items dropped from this block.
     *
     * @param world The current world
     * @param parPos the par pos
     * @param parState the par state
     * @param parFortune the par fortune
     * @return A ArrayList containing all items this block drops
     */
    @Override
    public ArrayList<ItemStack> getDrops(IBlockAccess world, BlockPos parPos, IBlockState parState, int parFortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        return ret;
    }

    /* (non-Javadoc)
     * @see net.minecraft.block.Block#canSilkHarvest(net.minecraft.world.World, net.minecraft.util.math.BlockPos, net.minecraft.block.state.IBlockState, net.minecraft.entity.player.EntityPlayer)
     */
    @Override
    public boolean canSilkHarvest(World parWorld, BlockPos parPos, IBlockState parState, EntityPlayer parPlayer)
    {
        return false;
    }

    /* (non-Javadoc)
     * @see net.minecraft.block.Block#canCreatureSpawn(net.minecraft.block.state.IBlockState, net.minecraft.world.IBlockAccess, net.minecraft.util.math.BlockPos, net.minecraft.entity.EntityLiving.SpawnPlacementType)
     */
    @Override
    public boolean canCreatureSpawn(IBlockState state, IBlockAccess parWorld, BlockPos parPos, SpawnPlacementType parType)
    {
        // TODO
        // probably want to limit by creature type
        return true;
    }

    /* (non-Javadoc)
     * @see net.minecraft.block.Block#shouldCheckWeakPower(net.minecraft.block.state.IBlockState, net.minecraft.world.IBlockAccess, net.minecraft.util.math.BlockPos, net.minecraft.util.EnumFacing)
     */
    @Override
    public boolean shouldCheckWeakPower(IBlockState state, IBlockAccess parWorld, BlockPos parPos, EnumFacing parSide)
    {
        return false;
    }

    /**
     * Checks if the specified tool type is efficient on this block, 
     * meaning that it digs at full speed.
     *
     * @param parType the par type
     * @param parState the par state
     * @return true, if is tool effective
     */
    @Override
    public boolean isToolEffective(String parType, IBlockState parState)
    {
        return false;
    }
}
