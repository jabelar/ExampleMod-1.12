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
import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBiomes 
{
    // instantiate Biomes
//	public final static BiomeCustom MY_COOL_BIOME = new BiomeCustom();

	public static final Set<Biome> SET_BIOMES = ImmutableSet.of(
//            MY_COOL_BIOME
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
		 * Register this mod's {@link Biome}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void onEvent(final RegistryEvent.Register<Biome> event) 
		{
			final IForgeRegistry<Biome> registry = event.getRegistry();

	        System.out.println("Registering biomes");
	        
	        // DEBUG
	        System.out.println("Registry key set = "+registry.getKeys());
	        System.out.println("Registry value list = "+registry.getValues());

			for (final Biome biome : SET_BIOMES) {
				registry.register(biome);
			}

//            BiomeManager.addBiome(BiomeType.DESERT, new BiomeEntry(ashDesert, 10));
//            BiomeManager.addSpawnBiome(ashDesert);
//            BiomeManager.addStrongholdBiome(ashDesert);
//            BiomeDictionary.addTypes(ashDesert, BiomeDictionary.Type.HOT);

			initialize();
		}
	}
}
