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

import com.blogspot.jabelarminecraft.examplemod.MainMod;

import net.minecraft.block.BlockIce;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// TODO: Auto-generated Javadoc
/**
 * @author jabelar
 *
 */
public class BlockCloud extends BlockIce
{
    public static final PropertyBool TRANSLUCENT = PropertyBool.create("translucent");

    /**
     * Instantiates a new block cloud.
     */
    public BlockCloud()
    {
        super();
        
        // DEBUG
        System.out.println("BlockCloud constructor");
        
        setLightLevel(3);
        setTickRandomly(false);
    }
    
    /* (non-Javadoc)
     * @see net.minecraft.block.Block#createBlockState()
     */
    @Override 
    protected BlockStateContainer createBlockState() 
    { 
        return new BlockStateContainer(this, new IProperty[] {TRANSLUCENT}); 
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
    
    /**
     * Queries if this block should render in a given layer.
     * ISmartBlockModel can use {@link net.minecraftforge.client.MinecraftForgeClient#getRenderLayer()} to alter their model based on layer.
     *
     * @param state the state
     * @param layer the layer
     * @return true, if successful
     */
   @Override
   public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
   {
        return layer == BlockRenderLayer.TRANSLUCENT;
   }
//    
//    @Override
//    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
//    {
//        return false;
//    }
//    
//

    /* (non-Javadoc)
 * @see net.minecraft.block.BlockBreakable#shouldSideBeRendered(net.minecraft.block.state.IBlockState, net.minecraft.world.IBlockAccess, net.minecraft.util.math.BlockPos, net.minecraft.util.EnumFacing)
 */
@Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return MainMod.proxy.shouldSideBeRendered(blockState, blockAccess, pos, side)
                || super.shouldSideBeRendered(blockState, blockAccess, pos, side);

    }

//
//    /**
//     * Chance that fire will spread and consume this block.
//     * 300 being a 100% chance, 0, being a 0% chance.
//     *
//     * @param world The current world
//     * @param x The blocks X position
//     * @param y The blocks Y position
//     * @param z The blocks Z position
//     * @param face The face that the fire is coming from
//     * @return A number ranging from 0 to 300 relating used to determine if the block will be consumed by fire
//     */
//    @Override
//    public int getFlammability(IBlockAccess world, BlockPos parPos, EnumFacing parSide)
//    {
//        return 0;
//    }
//
//    /**
//     * Currently only called by fire when it is on top of this block.
//     * Returning true will prevent the fire from naturally dying during updating.
//     * Also prevents firing from dying from rain.
//     *
//     * @param world The current world
//     * @param x The blocks X position
//     * @param y The blocks Y position
//     * @param z The blocks Z position
//     * @param metadata The blocks current metadata
//     * @param side The face that the fire is coming from
//     * @return True if this block sustains fire, meaning it will never go out.
//     */
//    @Override
//    public boolean isFireSource(World parWorld, BlockPos parPos, EnumFacing parSide)
//    {
//        return false;
//    }
//
//    /**
//     * Metadata and fortune sensitive version, this replaces the old (int meta, Random rand)
//     * version in 1.1.
//     *
//     * @param meta Blocks Metadata
//     * @param fortune Current item fortune level
//     * @param random Random number generator
//     * @return The number of items to drop
//     */
//    @Override
//    public int quantityDropped(IBlockState parState, int parFortune, Random parRandom)
//    {
//        /**
//         * Returns the usual quantity dropped by the block plus a bonus of 1 to 'i' (inclusive).
//         */
//        return 0;
//    }
//
//    /**
//     * This returns a complete list of items dropped from this block.
//     *
//     * @param world The current world
//     * @param x X Position
//     * @param y Y Position
//     * @param z Z Position
//     * @param metadata Current metadata
//     * @param fortune Breakers fortune level
//     * @return A ArrayList containing all items this block drops
//     */
//    @Override
//    public ArrayList<ItemStack> getDrops(IBlockAccess world, BlockPos parPos, IBlockState parState, int parFortune)
//    {
//        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
//        return ret;
//    }
//
//    @Override
//    public boolean canSilkHarvest(World parWorld, BlockPos parPos, IBlockState parState, EntityPlayer parPlayer)
//    {
//        return false;
//    }
//
//    @Override
//    public boolean canCreatureSpawn(IBlockState state, IBlockAccess parWorld, BlockPos parPos, SpawnPlacementType parType)
//    {
//        // TODO
//        // probably want to limit by creature type
//        return true;
//    }
//
//    @Override
//    public boolean shouldCheckWeakPower(IBlockState state, IBlockAccess parWorld, BlockPos parPos, EnumFacing parSide)
//    {
//        return false;
//    }
//
//    /**
//     * Checks if the specified tool type is efficient on this block, 
//     * meaning that it digs at full speed.
//     * 
//     * @param type
//     * @param metadata
//     * @return
//     */
//    @Override
//    public boolean isToolEffective(String parType, IBlockState parState)
//    {
//        return false;
//    }
    
    /**
 * Convert the given metadata into a BlockState for this Block.
 *
 * @param meta the meta
 * @return the state from meta
 */
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        if (meta == 1)
        {
            return getDefaultState().withProperty(TRANSLUCENT, Boolean.valueOf(true));
        }    
        else
        {
            return getDefaultState().withProperty(TRANSLUCENT, Boolean.valueOf(false));
        }
    }

    /**
     * Convert the BlockState into the correct metadata value.
     *
     * @param state the state
     * @return the meta from state
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
        if (state.getValue(TRANSLUCENT).booleanValue())
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }
}
