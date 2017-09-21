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
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

// TODO: Auto-generated Javadoc
// @ObjectHolder(MainMod.MODID)
public class ModPotions 
{
    // instantiate Potions
//	public final static PotionCustom MY_COOL_POTION = new PotionCustom();
	
	public static final Set<Potion> SET_POTIONS = ImmutableSet.of(
			);

	// instantiate Potion Types
//  public final static PotionTypeCustom MY_POTION_TYPE = new PotionTypeCustom();
			
	public static final Set<PotionType> SET_POTION_TYPES = ImmutableSet.of(
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
		 * Register this mod's {@link Potion}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void onEvent(final RegistryEvent.Register<Potion> event) 
		{
			final IForgeRegistry<Potion> registry = event.getRegistry();

	        System.out.println("Registering potions");

			for (final Potion Potion : SET_POTIONS) {
				registry.register(Potion);
			}

			initialize();
		}

		/**
		 * On type event.
		 *
		 * @param event the event
		 */
		@SubscribeEvent
		public static void onTypeEvent(final RegistryEvent.Register<PotionType> event) 
		{
			final IForgeRegistry<PotionType> registry = event.getRegistry();

	        System.out.println("Registering potion types");

			for (final PotionType potionType : SET_POTION_TYPES) {
				registry.register(potionType);
			}
		}
}
}
