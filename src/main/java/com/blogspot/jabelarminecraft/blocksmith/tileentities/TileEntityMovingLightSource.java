/**
    Copyright (C) 2015 by jabelar

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

package com.blogspot.jabelarminecraft.blocksmith.tileentities;

import com.blogspot.jabelarminecraft.blocksmith.registries.BlockRegistry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author jabelar
 *
 */
public class TileEntityMovingLightSource extends TileEntity implements ITickable
{
    public EntityPlayer thePlayer;
    
    public TileEntityMovingLightSource()
    {
        // after constructing the tile entity instance, remember to call the setPlayer() method.
//        // DEBUG
//        System.out.println("Constructing");
    }
    
    /**
     * This controls whether the tile entity gets replaced whenever the block state is changed.
     * Normally only want this when block actually is replaced.
     */
    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return (oldState.getBlock() != newSate.getBlock());
    }

    @Override
    public void update()
    {
        // check if player has moved away from the tile entity
        EntityPlayer thePlayer = world.getClosestPlayer(getPos().getX()+0.5D, getPos().getY()+0.5D, getPos().getZ()+0.5D, 2.0D, false);
        if (thePlayer == null)
        {
            if (world.getBlockState(getPos()).getBlock() == BlockRegistry.MOVING_LIGHT_SOURCE)
            {
                world.setBlockToAir(getPos());
            }
        }
        else if (thePlayer.getActiveItemStack().getItem() != Item.getItemFromBlock(Blocks.TORCH))
        {
            if (world.getBlockState(getPos()).getBlock() == BlockRegistry.MOVING_LIGHT_SOURCE)
            {
                world.setBlockToAir(getPos());
            }            
        }
    }  
    
    public void setPlayer(EntityPlayer parPlayer)
    {
        thePlayer = parPlayer;
    }
}
