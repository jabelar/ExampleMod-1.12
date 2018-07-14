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
package com.blogspot.jabelarminecraft.examplemod.init;

import java.util.List;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// TODO: Auto-generated Javadoc
@SideOnly(Side.CLIENT)
public class ModBlockColors implements IBlockColor
{
    public static final IBlockColor INSTANCE = new ModBlockColors();
    public static Random rand = new Random();
    
    /* (non-Javadoc)
     * @see net.minecraft.client.renderer.color.IBlockColor#colorMultiplier(net.minecraft.block.state.IBlockState, net.minecraft.world.IBlockAccess, net.minecraft.util.math.BlockPos, int)
     */
    @Override
    public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex)
    {
        List<EntityPlayer> players = Minecraft.getMinecraft().world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos.add(-3, 1, -3), pos.add(3, 2, 3)));
        if (!players.isEmpty())
        {
            return 0xC2EDFF;
        }
        else
        {
            return 0xFFFFFF;
        }
    }
    
    /**
     * Register block colors.
     */
    public static void registerBlockColors()
    {
        // DEBUG
        System.out.println("Registering block color handler");
        
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(INSTANCE, ModBlocks.cloud_leaves);
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(INSTANCE, ModBlocks.cloud);
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(INSTANCE, ModBlocks.cloud_grass);
    }
}

