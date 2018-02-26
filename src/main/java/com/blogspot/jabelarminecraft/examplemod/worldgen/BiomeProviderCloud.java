package com.blogspot.jabelarminecraft.examplemod.worldgen;

import com.blogspot.jabelarminecraft.examplemod.init.ModBiomes;

import net.minecraft.world.biome.BiomeProviderSingle;

public class BiomeProviderCloud extends BiomeProviderSingle
{
    public BiomeProviderCloud()
    {
        super(ModBiomes.cloud);
        
        // DEBUG
        System.out.println("Constructing BiomeProviderCloud");
    }
}
