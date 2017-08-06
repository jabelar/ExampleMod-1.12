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

import java.util.List;

import com.blogspot.jabelarminecraft.blocksmith.BlockSmith;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

/**
 * @author jabelar
 *
 */
public class DeconstructingInputQuantity 
{
    public static int getStackSizeNeeded(ItemStack parItemStack)
    {
        Item theItem = parItemStack.getItem();
        // Create recipes for some things that don't normally have them
        if (theItem == Items.ENCHANTED_BOOK)
        {
            if (BlockSmith.allowDeconstructEnchantedBooks)
            {
                return 1;
            }
            else
            {
                return 0;
            }
        }
        List<?> crafts = CraftingManager.getInstance().getRecipeList();
        for (int i = 0;i<crafts.size();i++)
        {
            IRecipe recipe = (IRecipe) crafts.get(i);
            if (recipe != null)
            {
                ItemStack outputItemStack = recipe.getRecipeOutput();
                // if found matching recipe
                if (outputItemStack != null)
                {                    
                    if (outputItemStack.getUnlocalizedName().equals(parItemStack.getUnlocalizedName()))
                    {
                        // DEBUG
                        System.out.println("getStackSizeNeeded() found matching recipe");
                        return adjustQuantity(theItem, outputItemStack.stackSize);
                    }
                }
            }
        }
        // DEBUG
        System.out.println("No matching recipe found!");
        return 0; // no recipe found
    }

    public static int adjustQuantity(Item theItem, int parDefaultQuantity)
    {
        // prevent some deconstructions that aren't realistic (like PAPER into reeds)
        if (!BlockSmith.allowDeconstructUnrealistic)
        {
            if (     theItem == Items.PAPER 
                  || theItem == Items.MELON_SEEDS
                  || theItem == Items.PUMPKIN_SEEDS
                  || theItem == Items.BREAD
                  || theItem == Items.CAKE
                )
            {
                // DEBUG
                System.out.println("Triying to deconstruct unrealistic item when not allowed");
                return 0;
            }
        }
        if (!BlockSmith.allowHorseArmorCrafting && 
                (    theItem == Items.SADDLE
                  || theItem == Items.IRON_HORSE_ARMOR
                  || theItem == Items.GOLDEN_HORSE_ARMOR
                  || theItem == Items.DIAMOND_HORSE_ARMOR
                )
            )
        {
            // DEBUG
            System.out.println("Triying to deconstruct horse armor or SADDLE item when not allowed");
            return 0;
        }
        if (!BlockSmith.allowPartialDeconstructing)
        {
            // DEBUG
            System.out.println("Don't look for partial deconstruct recipe when not allowed");
            return parDefaultQuantity;
        }
        if (       theItem == Items.OAK_DOOR
                || theItem == Items.SPRUCE_DOOR
                || theItem == Items.BIRCH_DOOR
                || theItem == Items.JUNGLE_DOOR
                || theItem == Items.ACACIA_DOOR
                || theItem == Items.DARK_OAK_DOOR
                || theItem == Items.IRON_DOOR
                || theItem == Items.PAPER
                || theItem == Items.STICK
                || theItem == Item.getItemFromBlock(Blocks.LADDER)
                || theItem == Items.ENCHANTED_BOOK                    
                || theItem == Item.getItemFromBlock(Blocks.OAK_FENCE)
                || theItem == Item.getItemFromBlock(Blocks.SPRUCE_FENCE)
                || theItem == Item.getItemFromBlock(Blocks.BIRCH_FENCE)
                || theItem == Item.getItemFromBlock(Blocks.JUNGLE_FENCE)
                || theItem == Item.getItemFromBlock(Blocks.ACACIA_FENCE)
                || theItem == Item.getItemFromBlock(Blocks.DARK_OAK_FENCE)
                || theItem == Item.getItemFromBlock(Blocks.NETHER_BRICK_FENCE)
                || theItem == Items.SIGN
                || theItem == Items.GLASS_BOTTLE
                || theItem == Item.getItemFromBlock(Blocks.COBBLESTONE_WALL)
                || theItem == Item.getItemFromBlock(Blocks.QUARTZ_BLOCK)
                || theItem == Item.getItemFromBlock(Blocks.STAINED_HARDENED_CLAY)
                || theItem == Item.getItemFromBlock(Blocks.OAK_STAIRS)
                || theItem == Item.getItemFromBlock(Blocks.SPRUCE_STAIRS)
                || theItem == Item.getItemFromBlock(Blocks.BIRCH_STAIRS)
                || theItem == Item.getItemFromBlock(Blocks.JUNGLE_STAIRS)
                || theItem == Item.getItemFromBlock(Blocks.ACACIA_STAIRS)
                || theItem == Item.getItemFromBlock(Blocks.DARK_OAK_STAIRS)
                || theItem == Item.getItemFromBlock(Blocks.STONE_STAIRS)
                || theItem == Item.getItemFromBlock(Blocks.SANDSTONE_STAIRS)
                || theItem == Item.getItemFromBlock(Blocks.NETHER_BRICK_STAIRS)
                || theItem == Item.getItemFromBlock(Blocks.RED_SANDSTONE_STAIRS)
                || theItem == Item.getItemFromBlock(Blocks.QUARTZ_STAIRS)
                || theItem == Item.getItemFromBlock(Blocks.STONE_BRICK_STAIRS)
                || theItem == Item.getItemFromBlock(Blocks.BRICK_STAIRS)
                )
        {
            return 1;
        }
        if (theItem == Items.PAPER
                || theItem == Item.getItemFromBlock(Blocks.WOODEN_SLAB)
                || theItem == Item.getItemFromBlock(Blocks.STONE_SLAB)
                || theItem == Item.getItemFromBlock(Blocks.STONE_SLAB2)
                )
        {
            return 2;
        }
        if (       theItem == Item.getItemFromBlock(Blocks.IRON_BARS)
                || theItem == Item.getItemFromBlock(Blocks.RAIL)
                || theItem == Item.getItemFromBlock(Blocks.GOLDEN_RAIL)
                || theItem == Item.getItemFromBlock(Blocks.ACTIVATOR_RAIL)
                || theItem == Item.getItemFromBlock(Blocks.DETECTOR_RAIL)
                || theItem == Item.getItemFromBlock(Blocks.GLASS_PANE)
                || theItem == Item.getItemFromBlock(Blocks.STAINED_GLASS_PANE)
                )
        {
            return 8;
        }
        return parDefaultQuantity;
    }
}
