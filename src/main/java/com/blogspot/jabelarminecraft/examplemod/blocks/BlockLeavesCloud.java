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

import com.blogspot.jabelarminecraft.examplemod.MainMod;
import com.blogspot.jabelarminecraft.examplemod.init.ModBlocks;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

// TODO: Auto-generated Javadoc
public class BlockLeavesCloud extends BlockLeaves
{

    /**
     * Instantiates a new block leaves cloud.
     */
    public BlockLeavesCloud()
    {
        MainMod.proxy.setGraphicsLevel(this, true);
        setDefaultState(blockState.getBaseState().withProperty(CHECK_DECAY, Boolean.valueOf(true)).withProperty(DECAYABLE, Boolean.valueOf(true)));
    }

    /* (non-Javadoc)
     * @see net.minecraft.block.BlockLeaves#dropApple(net.minecraft.world.World, net.minecraft.util.math.BlockPos, net.minecraft.block.state.IBlockState, int)
     */
    @Override
    protected void dropApple(World worldIn, BlockPos pos, IBlockState state, int chance)
    {
        if (worldIn.rand.nextInt(chance) == 0)
        {
            spawnAsEntity(worldIn, pos, new ItemStack(Items.APPLE));
        }
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
        return Item.getItemFromBlock(ModBlocks.cloud_sapling);
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

    /* (non-Javadoc)
     * @see net.minecraft.block.Block#getSilkTouchDrop(net.minecraft.block.state.IBlockState)
     */
    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state)
    {
        return new ItemStack(Item.getItemFromBlock(this));
    }

    /**
     * Convert the given metadata into a BlockState for this Block.
     *
     * @param meta the meta
     * @return the state from meta
     */
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(DECAYABLE, Boolean.valueOf((meta & 4) == 0)).withProperty(CHECK_DECAY, Boolean.valueOf((meta & 8) > 0));
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
        int i = 0;

        if (!state.getValue(DECAYABLE).booleanValue())
        {
            i |= 4;
        }

        if (state.getValue(CHECK_DECAY).booleanValue())
        {
            i |= 8;
        }

        return i;
    }

    /* (non-Javadoc)
     * @see net.minecraft.block.Block#createBlockState()
     */
    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {CHECK_DECAY, DECAYABLE});
    }

    /**
     * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It
     * returns the metadata of the dropped item based on the old metadata of the block.
     *
     * @param state the state
     * @return the int
     */
    @Override
    public int damageDropped(IBlockState state)
    {
        return 0;
    }

    /**
     * Spawns the block's drops in the world. By the time this is called the Block has possibly been set to air via
     * Block.removedByPlayer
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
        }
        else
        {
            super.harvestBlock(worldIn, player, pos, state, te, stack);
        }
    }

    /* (non-Javadoc)
     * @see net.minecraftforge.common.IShearable#onSheared(net.minecraft.item.ItemStack, net.minecraft.world.IBlockAccess, net.minecraft.util.math.BlockPos, int)
     */
    @Override
    public NonNullList<ItemStack> onSheared(ItemStack item, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune)
    {
        return NonNullList.withSize(1, new ItemStack(this));
    }
    
    /* (non-Javadoc)
     * @see net.minecraft.block.BlockLeaves#getWoodType(int)
     */
    @Override
    public EnumType getWoodType(int meta)
    {
        // TODO Auto-generated method stub
        return null;
    }
}