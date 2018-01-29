package com.blogspot.jabelarminecraft.examplemod.worldgen;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenRavine;

public class MapGenRavineCloud extends MapGenRavine
{
    @Override
    protected void addTunnel(long p_180707_1_, int p_180707_3_, int p_180707_4_, ChunkPrimer p_180707_5_, double p_180707_6_, double p_180707_8_, double p_180707_10_, float p_180707_12_, float p_180707_13_, float p_180707_14_, int p_180707_15_, int p_180707_16_, double p_180707_17_)
    {
//        // DEBUG
//        System.out.println("MapGenRavineCloud addTunnel()");
        
        super.addTunnel(p_180707_1_, p_180707_3_, p_180707_4_, p_180707_5_, p_180707_6_, p_180707_8_, p_180707_10_, p_180707_12_, p_180707_13_, p_180707_14_, p_180707_15_, p_180707_16_, p_180707_17_);
    }
    
    @Override
    public void generate(World worldIn, int x, int z, ChunkPrimer primer)
    {
//        // DEBUG
//        System.out.println("MapGenRavineCloud generate()");
        
        super.generate(worldIn, x, z, primer);
    }
}
