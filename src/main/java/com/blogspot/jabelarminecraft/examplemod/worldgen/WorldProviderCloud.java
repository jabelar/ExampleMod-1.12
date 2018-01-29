package com.blogspot.jabelarminecraft.examplemod.worldgen;

import com.blogspot.jabelarminecraft.examplemod.init.ModWorldGen;

import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldProviderCloud extends WorldProvider
{
    private String generatorSettings;

    @Override
    public DimensionType getDimensionType()
    {
        return ModWorldGen.CLOUD_DIM_TYPE;
    }

    @Override
    public IChunkGenerator createChunkGenerator()
    {
        return ModWorldGen.CLOUD_WORLD_TYPE.getChunkGenerator(world, generatorSettings);
    }
}
