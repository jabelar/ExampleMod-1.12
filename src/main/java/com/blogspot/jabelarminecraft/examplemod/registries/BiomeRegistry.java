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

import net.minecraft.block.Block;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(MainMod.MODID)
public class BiomeRegistry 
{
    // instantiate Biomes
//	public final static BiomeCustom MY_COOL_BIOME = new BiomeCustom();

	/**
	 * Initialize this mod's {@link Block}s with any post-registration data.
	 */
	private static void initialize() 
	{
	}

	@Mod.EventBusSubscriber(modid = MainMod.MODID)
	public static class RegistrationHandler 
	{
		public static final Set<Biome> SET_BIOMES = new HashSet<>();

		/**
		 * Register this mod's {@link Biome}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void onEvent(final RegistryEvent.Register<Biome> event) 
		{
			final Biome[] arrayBiomes = {
//					MY_COOL_BIOME
			};

			final IForgeRegistry<Biome> registry = event.getRegistry();

	        System.out.println("Registering biomes");

			for (final Biome Biome : arrayBiomes) {
				registry.register(Biome);
				SET_BIOMES.add(Biome);
			}

			initialize();
		}
	}
}
