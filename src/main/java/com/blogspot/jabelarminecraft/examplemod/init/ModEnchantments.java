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
import com.blogspot.jabelarminecraft.examplemod.enchantments.EnchantmentSafeFalling;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(MainMod.MODID)
public class ModEnchantments
{
    public static final Enchantment safe_falling = null;
            
    @Mod.EventBusSubscriber(modid = MainMod.MODID)
    public static class RegistrationHandler
    {
        
        /**
         * On event.
         *
         * @param event the event
         */
        @SubscribeEvent
        public static void onEvent(final RegistryEvent.Register<Enchantment> event)
        {
            // DEBUG
            System.out.println("Registering Enchantments");

            final IForgeRegistry<Enchantment> registry = event.getRegistry();
                        
            registry.register(new EnchantmentSafeFalling());            
        }
    }
}
