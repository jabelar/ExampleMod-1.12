package com.blogspot.jabelarminecraft.examplemod.recipes;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.play.server.SPacketRecipeBook;
import net.minecraft.stats.RecipeBookServer;

public class RecipeBookServerCustom extends RecipeBookServer
{
    @SuppressWarnings("deprecation")
    @Override
    public void add(List<IRecipe> recipesIn, EntityPlayerMP player)
    {
        List<IRecipe> list = Lists.<IRecipe>newArrayList();

        for (IRecipe irecipe : recipesIn)
        {
            if (!recipes.get(getRecipeId(irecipe)) && !irecipe.isDynamic())
            {
                if (!player.world.isDaytime()) // <--- Put whatever player condition here
                {
                    // DEBUG
                    System.out.println("Unlocking recipe");
                    
                    unlock(irecipe);
                    markNew(irecipe);
                    list.add(irecipe);
                    CriteriaTriggers.RECIPE_UNLOCKED.trigger(player, irecipe);
                    net.minecraftforge.common.ForgeHooks.sendRecipeBook(player.connection, SPacketRecipeBook.State.ADD, recipesIn, Collections.emptyList(), isGuiOpen, isFilteringCraftable);
               }
                else
                {
                    // DEBUG
                    System.out.println("Can't unlock recipe as player condition not met");
                }
            }
            else
            {
                net.minecraftforge.common.ForgeHooks.sendRecipeBook(player.connection, SPacketRecipeBook.State.ADD, recipesIn, Collections.emptyList(), isGuiOpen, isFilteringCraftable);
            }
        }
    }
}
