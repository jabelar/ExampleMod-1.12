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
package com.blogspot.jabelarminecraft.examplemod.blocks.fluids;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

// TODO: Auto-generated Javadoc
public class ModBlockFluidClassic extends BlockFluidClassic
{

	/**
	 * Instantiates a new mod block fluid classic.
	 *
	 * @param fluid the fluid
	 * @param material the material
	 */
	public ModBlockFluidClassic(Fluid fluid, Material material) 
	{
		super(fluid, material);
	}

	/* (non-Javadoc)
	 * @see net.minecraft.block.Block#getRenderType(net.minecraft.block.state.IBlockState)
	 */
	@Override
	public EnumBlockRenderType getRenderType(IBlockState parIBlockState)
	{
		return EnumBlockRenderType.LIQUID;
	}
}
