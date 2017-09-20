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
package com.blogspot.jabelarminecraft.examplemod.creativetabs;

import com.blogspot.jabelarminecraft.examplemod.MainMod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// TODO: Auto-generated Javadoc
public class CustomCreativeTab extends CreativeTabs {

	/**
	 * Instantiates a new custom creative tab.
	 */
	public CustomCreativeTab() 
	{
		super(MainMod.MODID);
	}

	/* (non-Javadoc)
	 * @see net.minecraft.creativetab.CreativeTabs#getTabIconItem()
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public ItemStack getTabIconItem() 
	{
		return new ItemStack(Items.BANNER);
	}

	/* (non-Javadoc)
	 * @see net.minecraft.creativetab.CreativeTabs#displayAllRelevantItems(net.minecraft.util.NonNullList)
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void displayAllRelevantItems(final NonNullList<ItemStack> items) 
	{
		super.displayAllRelevantItems(items);
	}
}