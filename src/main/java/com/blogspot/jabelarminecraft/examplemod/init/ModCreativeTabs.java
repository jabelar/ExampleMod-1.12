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

import com.blogspot.jabelarminecraft.examplemod.MainMod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// TODO: Auto-generated Javadoc
public class ModCreativeTabs
{
    // instantiate creative tabs
    public static final CustomCreativeTab CREATIVE_TAB = new CustomCreativeTab();

    /**
     * This class is used for an extra tab in the creative inventory. Many
     * mods like to group their special items and blocks in a dedicated tab
     * although it is also perfectly acceptable to put them in the vanilla tabs
     * where it makes sense.
     * */
    public static class CustomCreativeTab extends CreativeTabs
    {
        /**
         * Instantiates a new custom creative tab.
         */
        public CustomCreativeTab()
        {
            // pass a string for the tab label, if you only have one it is common
            // to pass the modid and then in your lang file you can put name of your mod.
            // The unlocalized name of a tab automatically has "itemGroup." prepended.
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
}