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
public class GrinderRecipes 
{
    private static final GrinderRecipes grindingBase = new GrinderRecipes();
    /** The list of grinding results. */
    private final Map grindingList = Maps.newHashMap();
    private final Map experienceList = Maps.newHashMap();

    public static GrinderRecipes instance()
    {
        return grindingBase;
    }

    private GrinderRecipes()
    {
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.STONEBRICK), 1, 32767), new ItemStack(Item.getItemFromBlock(Blocks.GRAVEL)), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.STONE_SLAB), 1, 32767), new ItemStack(Item.getItemFromBlock(Blocks.GRAVEL)), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.STONE_SLAB2), 1, 32767), new ItemStack(Item.getItemFromBlock(Blocks.GRAVEL)), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.SANDSTONE_STAIRS), 1, 32767), new ItemStack(Item.getItemFromBlock(Blocks.GRAVEL)), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.STONE), 1, 32767), new ItemStack(Item.getItemFromBlock(Blocks.GRAVEL)), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.GRAVEL), 1, 32767), new ItemStack(Item.getItemFromBlock(Blocks.SAND)), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.SANDSTONE), 1, 32767), new ItemStack(Item.getItemFromBlock(Blocks.SAND)), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.GLASS), 1, 32767), new ItemStack(Item.getItemFromBlock(Blocks.SAND)), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.BRICK_BLOCK), 1, 32767), new ItemStack(Item.getItemFromBlock(Blocks.GRAVEL)), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.PLANKS), 1, 32767), new ItemStack(Items.PAPER, 10), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.LOG), 1, 32767), new ItemStack(Items.PAPER), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.LOG2), 1, 32767), new ItemStack(Items.PAPER), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.NETHER_BRICK), 1, 32767), new ItemStack(Item.getItemFromBlock(Blocks.NETHERRACK)), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.NETHER_BRICK_STAIRS), 1, 32767), new ItemStack(Item.getItemFromBlock(Blocks.NETHERRACK)), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.NETHER_BRICK_FENCE), 1, 32767), new ItemStack(Item.getItemFromBlock(Blocks.NETHERRACK)), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.NETHERRACK), 1, 32767), new ItemStack(Item.getItemFromBlock(Blocks.SOUL_SAND)), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.SOUL_SAND), 1, 32767), new ItemStack(Items.GUNPOWDER, 4), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.SLIME_BLOCK), 1, 32767), new ItemStack(Items.SLIME_BALL, 9), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.OBSIDIAN), 1, 32767), new ItemStack(Items.FLINT, 10), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.PRISMARINE), 1, 32767), new ItemStack(Items.PRISMARINE_SHARD, 10), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.SEA_LANTERN), 1, 32767), new ItemStack(Items.PRISMARINE_CRYSTALS, 9), 0.7F);
    }

    public void addGrindingRecipe(ItemStack parItemStackIn, ItemStack parItemStackOut, float parExperience)
    {
        grindingList.put(parItemStackIn, parItemStackOut);
        experienceList.put(parItemStackOut, Float.valueOf(parExperience));
    }

    /**
     * Returns the grinding result of an item.
     */
    public ItemStack getGrindingResult(ItemStack parItemStack)
    {
        Iterator iterator = grindingList.entrySet().iterator();
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

    private boolean areItemStacksEqual(ItemStack parItemStack1, ItemStack parItemStack2)
    {
        return parItemStack2.getItem() == parItemStack1.getItem() && (parItemStack2.getMetadata() == 32767 || parItemStack2.getMetadata() == parItemStack1.getMetadata());
    }

    public Map getGrindingList()
    {
        return grindingList;
    }

    public float getGrindingExperience(ItemStack parItemStack)
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