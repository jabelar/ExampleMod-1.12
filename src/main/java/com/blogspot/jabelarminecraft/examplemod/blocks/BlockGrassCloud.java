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

import java.util.Random;

import javax.annotation.Nullable;

import com.blogspot.jabelarminecraft.examplemod.init.ModBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

// TODO: Auto-generated Javadoc
public class BlockGrassCloud extends BlockBush implements IGrowable, IShearable
{
    protected static final AxisAlignedBB TALL_GRASS_AABB = new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D,
            0.8D, 0.9D);

    /**
     * Instantiates a new block grass cloud.
     */
    public BlockGrassCloud()
    {
        super(Material.VINE);
    }

    /* (non-Javadoc)
     * @see net.minecraft.block.BlockBush#getBoundingBox(net.minecraft.block.state.IBlockState, net.minecraft.world.IBlockAccess, net.minecraft.util.math.BlockPos)
     */
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return TALL_GRASS_AABB;
    }

    /* (non-Javadoc)
     * @see net.minecraft.block.BlockBush#canBlockStay(net.minecraft.world.World, net.minecraft.util.math.BlockPos, net.minecraft.block.state.IBlockState)
     */
    @Override
    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    {
        return super.canBlockStay(worldIn, pos, state);
    }

    /**
     * Whether this Block can be replaced directly by other blocks (true for e.g. tall grass)
     *
     * @param worldIn the world in
     * @param pos the pos
     * @return true, if is replaceable
     */
    @Override
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
    {
        return true;
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *
     * @param state the state
     * @param rand the rand
     * @param fortune the fortune
     * @return the item dropped
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return null;
    }

    /**
     * Get the quantity dropped based on the given fortune level.
     *
     * @param fortune the fortune
     * @param random the random
     * @return the int
     */
    @Override
    public int quantityDroppedWithBonus(int fortune, Random random)
    {
        return 1 + random.nextInt(fortune * 2 + 1);
    }

    /**
     * Spawns the block's drops in the world. By the time this is called the Block has possibly been set to air via Block.removedByPlayer
     *
     * @param worldIn the world in
     * @param player the player
     * @param pos the pos
     * @param state the state
     * @param te the te
     * @param stack the stack
     */
    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        if (!worldIn.isRemote && stack.getItem() == Items.SHEARS)
        {
            player.addStat(StatList.getBlockStats(this));
            spawnAsEntity(worldIn, pos, new ItemStack(ModBlocks.cloud_grass));
        }
        else
        {
            super.harvestBlock(worldIn, player, pos, state, te, stack);
        }
    }

    /* (non-Javadoc)
     * @see net.minecraft.block.Block#getItem(net.minecraft.world.World, net.minecraft.util.math.BlockPos, net.minecraft.block.state.IBlockState)
     */
    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(this, 1, state.getBlock().getMetaFromState(state));
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks).
     *
     * @param itemIn the item in
     * @param items the items
     * @return the sub blocks
     */
    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
    {
        items.add(new ItemStack(this));
    }

    /**
     * Whether this IGrowable can grow.
     *
     * @param worldIn the world in
     * @param pos the pos
     * @param state the state
     * @param isClient the is client
     * @return true, if successful
     */
    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    {
        return true;
    }

    /* (non-Javadoc)
     * @see net.minecraft.block.IGrowable#canUseBonemeal(net.minecraft.world.World, java.util.Random, net.minecraft.util.math.BlockPos, net.minecraft.block.state.IBlockState)
     */
    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        return true;
    }

    /* (non-Javadoc)
     * @see net.minecraft.block.IGrowable#grow(net.minecraft.world.World, java.util.Random, net.minecraft.util.math.BlockPos, net.minecraft.block.state.IBlockState)
     */
    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        BlockDoublePlant.EnumPlantType doublePlantType = BlockDoublePlant.EnumPlantType.GRASS;

        if (Blocks.DOUBLE_PLANT.canPlaceBlockAt(worldIn, pos))
        {
            Blocks.DOUBLE_PLANT.placeAt(worldIn, pos, doublePlantType, 2);
        }
    }

    /**
     * Get the OffsetType for this Block. Determines if the model is rendered slightly offset.
     *
     * @return the offset type
     */
    @Override
    public Block.EnumOffsetType getOffsetType()
    {
        return Block.EnumOffsetType.XYZ;
    }

    /* (non-Javadoc)
     * @see net.minecraftforge.common.IShearable#isShearable(net.minecraft.item.ItemStack, net.minecraft.world.IBlockAccess, net.minecraft.util.math.BlockPos)
     */
    @Override
    public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos)
    {
        return true;
    }

    /* (non-Javadoc)
     * @see net.minecraftforge.common.IShearable#onSheared(net.minecraft.item.ItemStack, net.minecraft.world.IBlockAccess, net.minecraft.util.math.BlockPos, int)
     */
    @Override
    public NonNullList<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
    {
        return NonNullList.withSize(1, new ItemStack(ModBlocks.cloud_grass));
    }

    /* (non-Javadoc)
     * @see net.minecraft.block.Block#getDrops(net.minecraft.util.NonNullList, net.minecraft.world.IBlockAccess, net.minecraft.util.math.BlockPos, net.minecraft.block.state.IBlockState, int)
     */
    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        if (RANDOM.nextInt(8) != 0)
            return;
        ItemStack seed = net.minecraftforge.common.ForgeHooks.getGrassSeed(RANDOM, fortune);
        if (!seed.isEmpty())
            drops.add(seed);
    }
}
