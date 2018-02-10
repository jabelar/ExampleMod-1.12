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
import com.blogspot.jabelarminecraft.examplemod.blocks.BlockCloud;
import com.blogspot.jabelarminecraft.examplemod.blocks.BlockCloudBedrock;
import com.blogspot.jabelarminecraft.examplemod.blocks.BlockCompactor;
import com.blogspot.jabelarminecraft.examplemod.blocks.BlockLeavesCloud;
import com.blogspot.jabelarminecraft.examplemod.blocks.BlockLogCloud;
import com.blogspot.jabelarminecraft.examplemod.blocks.BlockSaplingCloud;
import com.blogspot.jabelarminecraft.examplemod.blocks.fluids.ModBlockFluidClassic;
import com.blogspot.jabelarminecraft.examplemod.utilities.Utilities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Instances and registration class
 * 
 * @author jabelar
 */
@ObjectHolder(MainMod.MODID)
public class ModBlocks
{
    /*
     * Block instances
     */
    public static final BlockCompactor compactor = null;
    public static final BlockFluidClassic slime = null;
    public static final BlockCloud cloud = null;
    public static final BlockCloudBedrock cloud_rock = null;
    public static final BlockFlower cloud_flower = null;
    public static final BlockLogCloud cloud_log = null;
    public static final BlockLeavesCloud cloud_leaves = null;
    public static final BlockSaplingCloud cloud_sapling = null;
    
    /*
     * ItemBlock instances
     */
    @ObjectHolder("compactor")
    public static final ItemBlock itemBlock_compactor = null;
    @ObjectHolder("slime")
    public static final ItemBlock itemBlock_slime = null;
    @ObjectHolder("cloud")
    public static final ItemBlock itemBlock_cloud = null;
    @ObjectHolder("cloud_rock")
    public static final ItemBlock itemBlock_cloud_rock = null;
    @ObjectHolder("cloud_flower")
    public static final ItemBlock itemBlock_cloud_flower = null;
    @ObjectHolder("cloud_log")
    public static final ItemBlock itemBlock_cloud_log = null;
    @ObjectHolder("cloud_leaves")
    public static final ItemBlock itemBlock_cloud_leaves = null;
    @ObjectHolder("cloud_sapling")
    public static final ItemBlock itemBlock_cloud_sapling = null;

    @Mod.EventBusSubscriber(modid = MainMod.MODID)
    public static class RegistrationHandler
    {
        /**
         * Register this mod's {@link Block}s.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void onEvent(final RegistryEvent.Register<Block> event)
        {
            // DEBUG
            System.out.println("Registering Blocks");

            final IForgeRegistry<Block> registry = event.getRegistry();
            
            registry.register(Utilities.setBlockName(new BlockCompactor(), "compactor"));
            registry.register(Utilities.setBlockName(new ModBlockFluidClassic(ModFluids.SLIME, ModMaterials.SLIME), "slime"));
            registry.register(Utilities.setBlockName(new BlockCloud(), "cloud"));
            registry.register(Utilities.setBlockName(new BlockCloudBedrock(), "cloud_rock"));
//            registry.register(Utilities.setBlockName(new BlockFlowerCloud(), "cloud_flower"));
            registry.register(Utilities.setBlockName(new BlockLogCloud(), "cloud_log"));
            registry.register(Utilities.setBlockName(new BlockLeavesCloud(), "cloud_leaves"));
            registry.register(Utilities.setBlockName(new BlockSaplingCloud(), "cloud_sapling"));
        }

        /**
         * Register this mod's {@link ItemBlock}s.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void registerItemBlocks(final RegistryEvent.Register<Item> event)
        {
            // DEBUG
            System.out.println("Registering ItemBlocks");
            
            final IForgeRegistry<Item> registry = event.getRegistry();
            
            registry.register(Utilities.setItemName(new ItemBlock(compactor), compactor.getRegistryName().getResourcePath()));
            registry.register(Utilities.setItemName(new ItemBlock(slime), slime.getRegistryName().getResourcePath()));
            registry.register(Utilities.setItemName(new ItemBlock(cloud), cloud.getRegistryName().getResourcePath()));
            registry.register(Utilities.setItemName(new ItemBlock(cloud_rock), cloud_rock.getRegistryName().getResourcePath()));
            registry.register(Utilities.setItemName(new ItemBlock(cloud_log), cloud_log.getRegistryName().getResourcePath()));
            registry.register(Utilities.setItemName(new ItemBlock(cloud_leaves), cloud_leaves.getRegistryName().getResourcePath()));
            registry.register(Utilities.setItemName(new ItemBlock(cloud_sapling), cloud_sapling.getRegistryName().getResourcePath()));
        }

        /**
         * On model event.
         *
         * @param event the event
         */
        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public static void onModelEvent(final ModelRegistryEvent event)
        {
            // DEBUG
            System.out.println("Registering block models");

            registerBlockModel(compactor);
            registerBlockModel(slime);
            registerBlockModel(cloud);
            registerBlockModel(cloud_rock);
            registerBlockModel(cloud_log);
            registerBlockModel(cloud_leaves);
            registerBlockModel(cloud_sapling);
            registerItemBlockModels();
        }
    }

    /**
     * Register block model.
     *
     * @param parBlock the par block
     */
    @SideOnly(Side.CLIENT)
    public static void registerBlockModel(Block parBlock)
    {
        registerBlockModel(parBlock, 0);
    }

    /**
     * Register block model.
     *
     * @param parBlock the par block
     * @param parMetaData the par meta data
     */
    @SideOnly(Side.CLIENT)
    public static void registerBlockModel(Block parBlock, int parMetaData)
    {
        // DEBUG
        System.out.println("Registering block model for"
                + ": " + parBlock.getRegistryName());

        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(parBlock), parMetaData,
                new ModelResourceLocation(MainMod.MODID + ":" + parBlock.getUnlocalizedName().substring(5), "inventory"));
    }

    /**
     * Register item block models.
     */
    @SideOnly(Side.CLIENT)
    public static void registerItemBlockModels()
    {
        registerItemBlockModel(itemBlock_compactor);
        registerItemBlockModel(itemBlock_slime);
        registerItemBlockModel(itemBlock_cloud);
        registerItemBlockModel(itemBlock_cloud_rock);
        registerItemBlockModel(itemBlock_cloud_leaves);
        registerItemBlockModel(itemBlock_cloud_sapling);
    }

    /**
     * Register block model.
     *
     * @param parBlock the par block
     */
    @SideOnly(Side.CLIENT)
    public static void registerItemBlockModel(ItemBlock parBlock)
    {
        registerItemBlockModel(parBlock, 0);
    }

    /**
     * Register block model.
     *
     * @param parBlock the par block
     * @param parMetaData the par meta data
     */
    @SideOnly(Side.CLIENT)
    public static void registerItemBlockModel(ItemBlock parBlock, int parMetaData)
    {
        // DEBUG
        System.out.println("Registering item block model for"
                + ": " + parBlock.getRegistryName());
        
        ModelLoader.setCustomModelResourceLocation(parBlock, parMetaData,
                new ModelResourceLocation(MainMod.MODID + ":" + parBlock.getUnlocalizedName().substring(5), "inventory"));
    }
}