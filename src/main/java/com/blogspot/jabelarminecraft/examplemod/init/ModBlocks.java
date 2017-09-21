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

import java.util.Set;

import com.blogspot.jabelarminecraft.examplemod.MainMod;
import com.blogspot.jabelarminecraft.examplemod.blocks.BlockCompactor;
import com.blogspot.jabelarminecraft.examplemod.utilities.Utilities;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

// TODO: Auto-generated Javadoc
// @ObjectHolder(MainMod.MODID)
public class ModBlocks {
    // instantiate blocks
	public final static BlockCompactor COMPACTOR = new BlockCompactor();
	
	/*
	 * fluid blocks
	 * Make sure you set registry name here
	 */
	public static final BlockFluidBase SLIME_BLOCK = (BlockFluidBase) Utilities.setBlockName(new BlockFluidClassic(ModFluids.SLIME, ModMaterials.SLIME), "slime").setCreativeTab(CreativeTabs.DECORATIONS);
	
	public static final Set<Block> SET_BLOCKS = ImmutableSet.of(
			COMPACTOR,
			SLIME_BLOCK
			);
	public static final Set<ItemBlock> SET_ITEM_BLOCKS = ImmutableSet.of(
			new ItemBlock(COMPACTOR),
			new ItemBlock(SLIME_BLOCK)
			);

	/**
	 * Initialize this mod's {@link Block}s with any post-registration data.
	 */
	private static void initialize() 
	{
	}

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
			final IForgeRegistry<Block> registry = event.getRegistry();

			for (final Block block : SET_BLOCKS) {
				registry.register(block);
				// DEBUG
				System.out.println("Registering block: "+block.getRegistryName());
			}
			
			initialize();
		}

		/**
		 * Register this mod's {@link ItemBlock}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void registerItemBlocks(final RegistryEvent.Register<Item> event) 
		{
			final IForgeRegistry<Item> registry = event.getRegistry();

			for (final ItemBlock item : SET_ITEM_BLOCKS) {
				final Block block = item.getBlock();
				final ResourceLocation registryName = Preconditions.checkNotNull(block.getRegistryName(), "Block %s has null registry name", block);
				registry.register(item.setRegistryName(registryName));
				// DEBUG
				System.out.println("Registering Item Block for "+registryName);			}
		}		
		
		/**
		 * On model event.
		 *
		 * @param event the event
		 */
		@SubscribeEvent
		public static void onModelEvent(final ModelRegistryEvent event) 
		{
			//DEBUG
			System.out.println("Registering block models");
			
			registerBlockModels();
			registerItemBlockModels();
		}
	}	
	
    /**
     * Register block models.
     */
    public static void registerBlockModels()
    {
		for (final Block block : SET_BLOCKS) {
			registerBlockModel(block);
			// DEBUG
			System.out.println("Registering block model for"
					+ ": "+block.getRegistryName());
		}        
    }
    
    /**
     * Register block model.
     *
     * @param parBlock the par block
     */
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
    public static void registerBlockModel(Block parBlock, int parMetaData)
    {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(parBlock), parMetaData, new ModelResourceLocation(MainMod.MODID + ":" + parBlock.getUnlocalizedName().substring(5), "inventory"));
    }

	
    /**
     * Register block models.
     */
    public static void registerItemBlockModels()
    {
		for (final ItemBlock block : SET_ITEM_BLOCKS) {
			registerItemBlockModel(block);
			// DEBUG
			System.out.println("Registering item block model for"
					+ ": "+block.getRegistryName());
		}        
    }
    
    /**
     * Register block model.
     *
     * @param parBlock the par block
     */
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
    public static void registerItemBlockModel(ItemBlock parBlock, int parMetaData)
    {
        ModelLoader.setCustomModelResourceLocation(parBlock, parMetaData, new ModelResourceLocation(MainMod.MODID + ":" + parBlock.getUnlocalizedName().substring(5), "inventory"));
    }
}