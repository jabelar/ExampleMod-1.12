package com.blogspot.jabelarminecraft.blocksmith.registries;

import java.util.HashSet;
import java.util.Set;

import com.blogspot.jabelarminecraft.blocksmith.MainMod;

import net.minecraft.block.Block;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(MainMod.MODID)
public class SoundEventRegistry1 
{
    // instantiate SoundEvents
//	public final static SoundEventCustom MY_COOL_SoundEvent = new SoundEventCustom();

	/**
	 * Initialize this mod's {@link Block}s with any post-registration data.
	 */
	private static void initialize() 
	{
	}

	@Mod.EventBusSubscriber(modid = MainMod.MODID)
	public static class RegistrationHandler 
	{
		public static final Set<SoundEvent> SET_SOUND_EVENTS = new HashSet<>();

		/**
		 * Register this mod's {@link SoundEvent}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void onEvent(final RegistryEvent.Register<SoundEvent> event) 
		{
			final SoundEvent[] arraySoundEvents = {
//					MY_COOL_SoundEvent
			};

			final IForgeRegistry<SoundEvent> registry = event.getRegistry();

	        System.out.println("Registering sound events");

	        for (final SoundEvent SoundEvent : arraySoundEvents) {
				registry.register(SoundEvent);
				SET_SOUND_EVENTS.add(SoundEvent);
			}

			initialize();
		}
	}
}
