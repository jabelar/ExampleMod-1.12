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
import com.blogspot.jabelarminecraft.examplemod.worldgen.BiomeCloud;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(MainMod.MODID)
public class ModBiomes
{
    // instantiate Biomes
    public final static BiomeCloud cloud = null;

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
            
            registry.register(new BiomeCloud().setRegistryName(MainMod.MODID, ModWorldGen.CLOUD_NAME));

            // DEBUG
            System.out.println("Registry key set = " + registry.getKeys());
            System.out.println("Registry value list = " + registry.getValues());
        }
    }
    
    /**
     * This method should be called during the "init" FML lifecycle 
     * because it must happen after object handler injection
     */
    public static void initBiomeManagerAndDictionary()
    {
        // DEBUG
        System.out.println("Initializing BiomeManager and BiomeDictionary");
        
        BiomeManager.addBiome(BiomeType.COOL, new BiomeEntry(cloud, 10));
        BiomeManager.addSpawnBiome(cloud);
        BiomeManager.addStrongholdBiome(cloud);
        BiomeManager.addVillageBiome(cloud, false);
        BiomeDictionary.addTypes(cloud, 
                BiomeDictionary.Type.COLD,
                BiomeDictionary.Type.DRY,
                BiomeDictionary.Type.MAGICAL
                );
    }
}
