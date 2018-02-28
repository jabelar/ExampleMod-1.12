package com.blogspot.jabelarminecraft.examplemod.worldgen;

import com.blogspot.jabelarminecraft.examplemod.init.ModWorldGen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;

public class WorldProviderCloud extends WorldProvider
{
    @Override
    public DimensionType getDimensionType()
    {
        return ModWorldGen.CLOUD_DIM_TYPE;
    }

    /**
     * Returns 'true' if in the "main surface world", but 'false' if in the Nether or End dimensions.
     */
    @Override
    public boolean isSurfaceWorld()
    {
        return true;
    }

    /*======================================= Forge Start =========================================*/

    @Override
    public boolean canDoLightning(net.minecraft.world.chunk.Chunk chunk)
    {
        return true;
    }

    @Override
    public boolean canDoRainSnowIce(Chunk chunk)
    {
        return false;
    }
    
    @Override
    public boolean canSnowAt(BlockPos pos, boolean checkLight)
    {
        return false; 
    }
}
