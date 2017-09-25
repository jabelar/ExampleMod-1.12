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

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class ModSounds 
{
    // instantiate SoundEvents
	
	// big cats
//    public static final SoundEvent soundAmbientGrowlBigCat = null;
       
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
		 * Register this mod's {@link SoundEvent}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void onEvent(final RegistryEvent.Register<SoundEvent> event) 
		{
			final String[] arraySoundEvents = {
//					// big cats
//					"mob.bigCat.growl",
//					"mob.bigCat.whine",
//					"mob.bigCat.panting",
//					"mob.bigCat.park",
//					"mob.bigCat.hurt",
//					"mob.bigCat.death",
//					"mob.bigCat.shake",
//				    
//				    // elephant
//					"mob.elephant.say",
//					"mob.elephant.hurt",
//
//				    // birds of prey
//					"mob.birdsofprey.hurt",
//					"mob.birdsofprey.call",
//					"mob.birdsofprey.flapping",
//
//				    // serpent
//					"mob.serpent.death",
//					"mob.serpent.hiss"
			};

			final IForgeRegistry<SoundEvent> registry = event.getRegistry();

	        System.out.println("Registering sound events");

	        for (final String soundName : arraySoundEvents) 
	        {
				registry.register(new SoundEvent(new ResourceLocation(MainMod.MODID, soundName)).setRegistryName(soundName));
			}

			initialize();
		}
	}
}
