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
package com.blogspot.jabelarminecraft.examplemod.registries;

import java.util.HashSet;
import java.util.Set;

import com.blogspot.jabelarminecraft.examplemod.MainMod;
import com.blogspot.jabelarminecraft.examplemod.blocks.BlockCompactor;
import com.google.common.base.Preconditions;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

// TODO: Auto-generated Javadoc
@ObjectHolder(MainMod.MODID)
public class BlockRegistry {
//	public static class ArmorMaterials {
//		public static final BlockArmor.ArmorMaterial ARMOUR_MATERIAL_REPLACEMENT = EnumHelper.addArmorMaterial(Constants.RESOURCE_PREFIX + "replacement", Constants.RESOURCE_PREFIX + "replacement", 15, new int[]{1, 4, 5, 2}, 12, SoundEvents.Block_ARMOR_EQUIP_CHAIN, (float) 0);
//	}
//
//	public static class ToolMaterials {
//		public static final Block.ToolMaterial TOOL_MATERIAL_GLOWSTONE = EnumHelper.addToolMaterial("glowstone", 1, 5, 0.5f, 1.0f, 10);
//	}

    // instantiate blocks
	public final static BlockCompactor COMPACTOR = new BlockCompactor();

	/**
	 * Initialize this mod's {@link Block}s with any post-registration data.
	 */
	private static void initialize() 
	{
	}

	@Mod.EventBusSubscriber(modid = MainMod.MODID)
	public static class RegistrationHandler 
	{
		public static final Set<Block> SET_BLOCKS = new HashSet<>();
		public static final Set<Item> SET_ITEM_BLOCKS = new HashSet<>();

		/**
		 * Register this mod's {@link Block}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void onEvent(final RegistryEvent.Register<Block> event) 
		{
			final Block[] arrayBlocks = {
					COMPACTOR
			};

			final IForgeRegistry<Block> registry = event.getRegistry();

			for (final Block block : arrayBlocks) {
				registry.register(block);
				SET_BLOCKS.add(block);
				// DEBUG
				System.out.println("Registering block: "+block.getRegistryName());
			}
			
			registerBlockModels();

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
			final ItemBlock[] items = {
					new ItemBlock(COMPACTOR)
			};

			final IForgeRegistry<Item> registry = event.getRegistry();

			for (final ItemBlock item : items) {
				final Block block = item.getBlock();
				final ResourceLocation registryName = Preconditions.checkNotNull(block.getRegistryName(), "Block %s has null registry name", block);
				registry.register(item.setRegistryName(registryName));
				SET_ITEM_BLOCKS.add(item);
				// DEBUG
				System.out.println("Registering Item Block for "+registryName);			}
		}		
	}	
	
    /**
     * Register block models.
     */
    public static void registerBlockModels()
    {
        // DEBUG
        System.out.println("Registering block renderers");
        
        registerBlockModel(COMPACTOR);
        
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
}