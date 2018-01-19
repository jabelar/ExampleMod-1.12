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
import com.blogspot.jabelarminecraft.examplemod.entities.EntityPigTest;
import com.google.common.collect.ImmutableSet;

import net.minecraft.block.material.MapColor;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.registries.IForgeRegistry;

public class ModEntities
{
    // instantiate EntityEntry list
    public static final Set<EntityEntry> SET_ENTITIES = ImmutableSet.of(
            EntityEntryBuilder.create()
            .entity(EntityPigTest.class)
            .id(new ResourceLocation(MainMod.MODID, "test_pig"), 0)
            .name("test_pig")
            .tracker(80, 3, false)
            .egg(MapColor.BLUE.colorValue, MapColor.YELLOW.colorValue)
            .spawn(EnumCreatureType.CREATURE, 20, 1, 5, BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST))
            .build()
            );

    @EventBusSubscriber(modid = MainMod.MODID)
    public static class RegistrationHandler
    {
        /**
         * Register this mod's {@link EntityEntry}s.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void onEvent(final RegistryEvent.Register<EntityEntry> event)
        {
            final IForgeRegistry<EntityEntry> registry = event.getRegistry();

            // DEBUG
            System.out.println("Registering entities");

            for (final EntityEntry entityEntry : SET_ENTITIES)
            {
                // DEBUG
                System.out.println("Registering entity = " + entityEntry.getEntityClass());

                registry.register(entityEntry);
            }
        }
    }
}
