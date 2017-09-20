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

package com.blogspot.jabelarminecraft.examplemod.materials;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

// TODO: Auto-generated Javadoc
/**
 * @author jabelar
 *
 */
public class MaterialTanningRack extends Material
{
	
	/**
	 * Instantiates a new material tanning rack.
	 */
	public MaterialTanningRack() 
	{
		super(MapColor.SNOW);
	}

    /**
     * Returns if blocks of these materials are liquids.
     *
     * @return true, if is liquid
     */
    @Override
	public boolean isLiquid()
    {
        return false;
    }

    /* (non-Javadoc)
     * @see net.minecraft.block.material.Material#isSolid()
     */
    @Override
	public boolean isSolid()
    {
        return false;
    }
    
    /**
     * Returns if this material is considered solid or not.
     *
     * @return true, if successful
     */
    @Override
	public boolean blocksMovement()
    {
        return true;
    }

    /**
     * Returns if the block can burn or not.
     *
     * @return the can burn
     */
    @Override
	public boolean getCanBurn()
    {
        return false;
    }

    /**
     * Returns whether the material can be replaced by other blocks when placed - eg snow, vines and tall grass.
     *
     * @return true, if is replaceable
     */
    @Override
	public boolean isReplaceable()
    {
        return false;
    }

    /**
     * Indicate if the material is opaque.
     *
     * @return true, if is opaque
     */
    @Override
	public boolean isOpaque()
    {
        return false;
    }

    /**
     * Returns true if the material can be harvested without a tool (or with the wrong tool).
     *
     * @return true, if is tool not required
     */
    @Override
	public boolean isToolNotRequired()
    {
        return false;
    }

    /**
     * Returns the mobility information of the material, 0 = free, 1 = can't push but can move over, 2 = total
     * immobility and stop pistons.
     *
     * @return the mobility flag
     */
    @Override
	public EnumPushReaction getMobilityFlag()
    {
        return EnumPushReaction.NORMAL;
    }
}
