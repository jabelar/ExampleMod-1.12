package com.blogspot.jabelarminecraft.examplemod.worldgen;

import com.blogspot.jabelarminecraft.examplemod.init.ModWorldGen;

import net.minecraft.world.biome.Biome;

public class BiomeCloud extends Biome
{
    public BiomeCloud()
    {
        super(new BiomeProperties(ModWorldGen.CLOUD_NAME)
                .setBaseHeight(1.0F)
                .setHeightVariation(0.2F)
                .setRainDisabled()
                .setTemperature(0.2F)
                );
    }
}
