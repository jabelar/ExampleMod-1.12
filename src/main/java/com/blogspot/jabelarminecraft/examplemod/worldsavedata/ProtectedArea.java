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
package com.blogspot.jabelarminecraft.examplemod.worldsavedata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;

// TODO: Auto-generated Javadoc
public class ProtectedArea 
{
	private String name;
	private List<BlockPos> listBlocks = new ArrayList<BlockPos>();

	/**
	 * Instantiates a new protected area.
	 *
	 * @param parName the par name
	 */
	public ProtectedArea(String parName)
	{
		name = parName;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() { return name; }
	
	/**
	 * Adds the block position to the protected blocks list.
	 *
	 * @param parPos the par pos
	 */
	public void addBlock(BlockPos parPos)
	{
		listBlocks.add(parPos);
	}
	
	/**
	 * Removes the block position from the protected blocks list.
	 *
	 * @param parPos the par pos
	 */
	public void removeBlock(BlockPos parPos)
	{
		listBlocks.remove(parPos);
	}
	
	/**
	 * Clears the protected blocks.
	 */
	public void clearBlocks()
	{
		listBlocks.clear();
	}
	
	/**
	 * Gets the protected block list.
	 *
	 * @return the protected block list
	 */
	public List<BlockPos> getProtectedBlockList() { return listBlocks; }
	
	/**
	 * Gets the block list tag.
	 *
	 * @return the block list tag
	 */
	public NBTTagList getBlockListTag()
	{
		NBTTagList tagList = new NBTTagList();
		Iterator<BlockPos> iterator = listBlocks.iterator();
		while (iterator.hasNext())
		{
			BlockPos pos = iterator.next();
			NBTTagCompound posCompound = new NBTTagCompound();
			posCompound.setInteger("x", pos.getX());
			posCompound.setInteger("y", pos.getY());
			posCompound.setInteger("z", pos.getZ());
			tagList.appendTag(posCompound);
		}
		return tagList;
	}
}
