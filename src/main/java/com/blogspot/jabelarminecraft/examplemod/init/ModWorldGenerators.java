package com.blogspot.jabelarminecraft.examplemod.init;

import com.blogspot.jabelarminecraft.examplemod.worldgen.WorldGenShrine;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModWorldGenerators
{
    /**
     * Register world generators.
     */
    public static void registerWorldGenerators()
    {
        // DEBUG
        System.out.println("Registering world generators");
        GameRegistry.registerWorldGenerator(new WorldGenShrine(), 10);
    }
}
