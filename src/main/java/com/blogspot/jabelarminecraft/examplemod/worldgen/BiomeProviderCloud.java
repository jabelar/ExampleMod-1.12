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
    
//    @Override
//    public boolean areBiomesViable(int x, int z, int radius, List<Biome> allowed)
//    {
//        System.out.println("List of allowed biomes = "+allowed);
//        return allowed.contains(getBiome(new BlockPos(x, 64, z)));
//    }
}
