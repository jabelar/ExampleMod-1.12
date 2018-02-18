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
import com.blogspot.jabelarminecraft.examplemod.client.models.ModelSlimeBag;
import com.blogspot.jabelarminecraft.examplemod.items.ItemCowHide;
import com.blogspot.jabelarminecraft.examplemod.items.ItemHorseHide;
import com.blogspot.jabelarminecraft.examplemod.items.ItemPigSkin;
import com.blogspot.jabelarminecraft.examplemod.items.ItemSheepSkin;
import com.blogspot.jabelarminecraft.examplemod.items.ItemSwordExtended;
import com.blogspot.jabelarminecraft.examplemod.items.fluidcontainers.ItemSlimeBag;
import com.blogspot.jabelarminecraft.examplemod.utilities.Utilities;

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Instances and registration class
 * 
 * @author jabelar
 */
@ObjectHolder(MainMod.MODID)
public class ModItems
{
    public final static ItemCowHide cow_hide = null;
    public final static ItemSheepSkin sheep_skin = null;
    public final static ItemPigSkin pig_skin = null;
    public final static ItemHorseHide horse_hide = null;
    public final static ItemSwordExtended sword_extended = null;
    public final static ItemSlimeBag slime_bag = null;

    @Mod.EventBusSubscriber(modid = MainMod.MODID)
    public static class RegistrationHandler
    {
        /**
         * Register this mod's {@link Item}s.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void onEvent(final RegistryEvent.Register<Item> event)
        {
            final IForgeRegistry<Item> registry = event.getRegistry();

            System.out.println("Registering items");

            registry.register(Utilities.setItemName(new ItemCowHide(), "cow_hide"));
            registry.register(Utilities.setItemName(new ItemSheepSkin(), "sheep_skin"));
            registry.register(Utilities.setItemName(new ItemPigSkin(), "pig_skin"));
            registry.register(Utilities.setItemName(new ItemHorseHide(), "horse_hide"));
            registry.register(Utilities.setItemName(new ItemSwordExtended(ToolMaterial.IRON), "sword_extended"));
            registry.register(Utilities.setItemName(new ItemSlimeBag(), "slime_bag"));
        }

        /**
         * ModelRegistryEvent handler.
         *
         * @param event
         *            the event
         */
        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public static void onModelEvent(final ModelRegistryEvent event)
        {
            // DEBUG
            System.out.println("Registering item models");

            /*
             *  Register standard model items
             */
            registerItemModel(cow_hide);
            registerItemModel(sheep_skin);
            registerItemModel(pig_skin);
            registerItemModel(horse_hide);
            registerItemModel(sword_extended);

            /*
             *  Register custom model items
             */
            // DEBUG
            System.out.println("Registering item model for: " + slime_bag.getRegistryName());
            ModelLoaderRegistry.registerLoader(ModelSlimeBag.CustomModelLoader.INSTANCE);
            ModelLoader.setCustomMeshDefinition(slime_bag, stack -> ModelSlimeBag.LOCATION);
            ModelBakery.registerItemVariants(slime_bag, ModelSlimeBag.LOCATION);
        }

        /**
         * ModelBakeEvent handler.
         *
         * @param event
         *            the event
         */
        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public static void onModelEvent(final ModelBakeEvent event)
        {
            // DEBUG
            System.out.println("Models have been baked");
        }
    }

    /**
     * Register item model.
     *
     * @param parItem
     *            the par item
     */
    @SideOnly(Side.CLIENT)
    public static void registerItemModel(Item parItem)
    {
        registerItemModel(parItem, 0);
    }

    /**
     * Register item model.
     *
     * @param parItem the par item
     * @param parMetaData the par meta data
     */
    @SideOnly(Side.CLIENT)
    public static void registerItemModel(Item parItem, int parMetaData)
    {
        // DEBUG
        System.out.println("Registering item model for: " + parItem.getRegistryName());

        ModelLoader.setCustomModelResourceLocation(parItem, parMetaData,
                new ModelResourceLocation(MainMod.MODID + ":" + parItem.getUnlocalizedName().substring(5), "inventory"));
    }
    
    public static void registerOreDictionaryEntries()
    {
        /*
         * Look in the OreDictionary class to check the strings for vanilla items.
         */
        OreDictionary.registerOre("blockSlime", slime_bag);
    }
}
