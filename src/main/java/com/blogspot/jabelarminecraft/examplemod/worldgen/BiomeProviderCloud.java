package com.blogspot.jabelarminecraft.examplemod.worldgen;

import net.minecraft.world.biome.BiomeProviderSingle;

public class BiomeProviderCloud extends BiomeProviderSingle
{
    public BiomeProviderCloud()
    {
        super(new BiomeCloud());
    }
}
