/**
    Copyright (C) 2014 by jabelar

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

package com.blogspot.jabelarminecraft.examplemod.structures;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

/**
 * @author jabelar
 *
 */
public interface IStructure 
{
	/**
	 * Name of the structure, not used at this time, but potentially useful
	 * @return
	 */
	String getName();
	
	/**
	 * Size of structure in X dimension, based on array read from file
	 * @return
	 */
	int getDimX();
				
	/**
	 * Size of structure in Y dimension, based on array read from file
	 * @return
	 */
	int getDimY();
				
	/**
	 * Size of structure in Z dimension, based on array read from file
	 * @return
	 */
	int getDimZ();
	
	/**
	 * Should read from an asset file and populate a local array of strings
	 * with the block names, later used by generateTick() to place blocks
	 * @return
	 */
	String[][][] getBlockNameArray();
	
	/**
	 * Should read from an asset file and populate a local array of metadata
	 * corresponding to the same block positions in the block name array,
	 * later used by generateTick() to place blocks that have metadata.
	 * @return
	 */
	int[][][] getBlockMetaArray();

	/**
	 * Read from an asset file and populate the name array and metadata array
	 * @param parName
	 */
	void readArrays(String parName);

	/**
	 * Generate a portion of the structure per tick to minimize lag
	 * Suggest that at least one dimension is iterated over ticks rather than loop within tick.
	 * This method should call 1) generateBasicBlocksTick(), 2) generateMetaBlocksTick(), 
	 * 3) generateSpecialBlocksTick(), 4) populateItems(), 5) populateEntities()
	 * @param tileEntityMagicBeanStalk
	 * @param parOffsetX
	 * @param parOffsetY
	 * @param parOffsetZ
	 */
	void generateTick(TileEntity parEntity, int parOffsetX, int parOffsetY, int parOffsetZ);
	
	/**
	 * Generates "basic" blocks, meaning blocks with metadata value 0 that are not special
	 * like trip wire (which require other blocks placed first).
	 */
	void generateBasicBlocksTick();

	/**
	 * Generates blocks with metadata values.
	 */
	void generateMetaBlocksTick();
	
	/**
	 * Generates blocks that are special due to requiring other blocks to be placed first
	 * like trip wires.
	 */
	void generateSpecialBlocksTick();
	
	/**
	 * Put items on floor or in tile entity inventories (chests, furnace, etc.)
	 */
	void populateItems();
	
	/**
	 * Spawn any entities that may inhabit the structure by default
	 */
	void populateEntities();

	/**
	 * During block placement, if block has a tile entity, process it.
	 * For example you can put things in inventory, etc.
	 */
	void customizeTileEntity(BlockPos parPos);
}
