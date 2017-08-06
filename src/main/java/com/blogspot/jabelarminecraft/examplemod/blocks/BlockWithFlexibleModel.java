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

package com.blogspot.jabelarminecraft.examplemod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

/**
 * @author jabelar
 *
 */
public class BlockWithFlexibleModel extends Block 
{

	// change to your specific properties
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	/**
	 * @param materialIn
	 */
	protected BlockWithFlexibleModel(Material materialIn) 
	{
		super(materialIn);
		// TODO Auto-generated constructor stub
	}

	/*
	 * See tutorial from herbix at http://www.minecraftforge.net/forum/index.php/topic,28714.0.html
	 */
	public static final IUnlistedProperty<Integer> JAI = new IUnlistedProperty<Integer>() 
	{
        @Override
        public String getName() 
        {
            return "justAnotherInteger";
        }
        
        @Override
        public boolean isValid(Integer value) 
        {
            return true;
        }
        
        @Override
        public Class<Integer> getType() 
        {
            return Integer.class;
        }
        
        @Override
        public String valueToString(Integer value) 
        {
            return value.toString();
        }
    };

    @Override
    protected BlockStateContainer createBlockState() 
    {
        return new ExtendedBlockState(this, new IProperty[] { FACING  }, new IUnlistedProperty[]{ JAI });
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) 
    {
        if(state instanceof IExtendedBlockState) {
            return ((IExtendedBlockState)state).withProperty(JAI, pos.getY());
        }
        return state;
    }
}