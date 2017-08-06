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

package com.blogspot.jabelarminecraft.blocksmith.recipes;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * @author jabelar
 *
 */
public class CompactorRecipes 
{
    private static final CompactorRecipes compactingBase = new CompactorRecipes();
    /** The list of compacting results. */
    private final Map compactingList = Maps.newHashMap();
    private final Map experienceList = Maps.newHashMap();

    public static CompactorRecipes instance()
    {
        return compactingBase;
    }

    private CompactorRecipes()
    {
        addCompactingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.SAND), 2, 0), new ItemStack(Item.getItemFromBlock(Blocks.GRAVEL)), 0.7F);
        addCompactingRecipe(new ItemStack(Items.SLIME_BALL, 4, 0), new ItemStack(Item.getItemFromBlock(Blocks.SLIME_BLOCK)), 0.7F);
        addCompactingRecipe(new ItemStack(Items.BLAZE_POWDER, 2, 0), new ItemStack(Items.BLAZE_ROD), 0.7F);
    }

    public void addCompactingRecipe(ItemStack parItemStackIn, ItemStack parItemStackOut, float parExperience)
    {
        compactingList.put(parItemStackIn, parItemStackOut);
        experienceList.put(parItemStackOut, Float.valueOf(parExperience));
    }

    /**
     * Returns the compacting result of an item.
     */
    public ItemStack getCompactingResult(ItemStack parItemStack)
    {
        Iterator iterator = compactingList.entrySet().iterator();
        Entry entry;

        do
        {
            if (!iterator.hasNext())
            {
                return null;
            }

            entry = (Entry)iterator.next();
        }
        while (!areItemStacksEqual(parItemStack, (ItemStack)entry.getKey()));

        return (ItemStack)entry.getValue();
    }
    
    // allows recipe to consume multiple items from input stack
    public int getInputAmount(ItemStack parItemStack)
    {
        Iterator iterator = compactingList.entrySet().iterator();
        Entry entry;

        do
        {
            if (!iterator.hasNext())
            {
                return -1;
            }

            entry = (Entry)iterator.next();
        }
        while (!areItemStacksEqual(parItemStack, (ItemStack)entry.getKey()));

        return ((ItemStack)entry.getKey()).getCount();
    }

    private boolean areItemStacksEqual(ItemStack parItemStack1, ItemStack parItemStack2)
    {
        return parItemStack2.getItem() == parItemStack1.getItem() && (parItemStack2.getMetadata() == parItemStack1.getMetadata());
    }

    public Map getCompactingList()
    {
        return compactingList;
    }

    public float getCompactingExperience(ItemStack parItemStack)
    {
        Iterator iterator = experienceList.entrySet().iterator();
        Entry entry;

        do
        {
            if (!iterator.hasNext())
            {
                return 0.0F;
            }

            entry = (Entry)iterator.next();
        }
        while (!areItemStacksEqual(parItemStack, (ItemStack)entry.getKey()));

        return ((Float)entry.getValue()).floatValue();
    }
}