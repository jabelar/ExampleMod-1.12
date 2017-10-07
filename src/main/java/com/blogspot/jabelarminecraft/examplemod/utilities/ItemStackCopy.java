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
package com.blogspot.jabelarminecraft.examplemod.utilities;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

// TODO: Auto-generated Javadoc
/*
 *  This class holds (one at a time) and returns all the information about an ItemStack
 *  without actually instantiating one. This avoids a lot of unnecessary
 *  performance hit when making an actual copy of an ItemStack as there 
 *  are various methods like initCapabilities which can get called otherwise.
 */

public class ItemStackCopy 
{
	private static int meta;
	private static int count;
	private static NBTTagCompound compound;
	
	/**
	 * Store.
	 *
	 * @param parStack the par stack
	 */
	public static void store(ItemStack parStack)
	{
		meta = parStack.getItemDamage();
		count = parStack.getCount();
		compound = parStack.getTagCompound();
	}
	
	/**
	 * Restore.
	 *
	 * @param parStack the par stack
	 * @return the item stack
	 */
	public static ItemStack restore(ItemStack parStack)
	{
		parStack.setCount(count);
		parStack.setItemDamage(meta);
		parStack.setTagCompound(compound);
		return parStack;
	}
}
