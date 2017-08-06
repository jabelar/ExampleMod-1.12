package com.blogspot.jabelarminecraft.blocksmith.registries;

import java.util.HashSet;
import java.util.Set;

import com.blogspot.jabelarminecraft.blocksmith.BlockSmith;
import net.minecraft.block.Block;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(BlockSmith.MODID)
public class PotionRegistry 
{
    // instantiate Potions
//	public final static PotionCustom MY_COOL_Potion = new PotionCustom();

	/**
	 * Initialize this mod's {@link Block}s with any post-registration data.
	 */
	private static void initialize() 
	{
	}

	@Mod.EventBusSubscriber(modid = BlockSmith.MODID)
	public static class RegistrationHandler 
	{
		public static final Set<Potion> SET_POTIONS = new HashSet<>();

		/**
		 * Register this mod's {@link Potion}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void onEvent(final RegistryEvent.Register<Potion> event) 
		{
			final Potion[] arrayPotions = {
//					MY_COOL_Potion
			};

			final IForgeRegistry<Potion> registry = event.getRegistry();

	        System.out.println("Registering potions");

			for (final Potion Potion : arrayPotions) {
				registry.register(Potion);
				SET_POTIONS.add(Potion);
			}

			initialize();
		}
	}
}
