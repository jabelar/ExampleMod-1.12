package com.blogspot.jabelarminecraft.examplemod.worldgen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenCaves;

public class MapGenCavesCloud extends MapGenCaves
{
    @Override
    protected void addTunnel(long p_180702_1_, int p_180702_3_, int p_180702_4_, ChunkPrimer p_180702_5_, double p_180702_6_, double p_180702_8_, double p_180702_10_, float p_180702_12_, float p_180702_13_, float p_180702_14_, int p_180702_15_, int p_180702_16_, double p_180702_17_)
    {
//        // DEBUG
//        System.out.println("MapGenCavesCloud addTunnel()");
        super.addTunnel(p_180702_1_, p_180702_3_, p_180702_4_, p_180702_5_, p_180702_6_, p_180702_8_, p_180702_10_, p_180702_12_, p_180702_13_, p_180702_14_, p_180702_15_, p_180702_16_, p_180702_17_);
    }
    @Override
    protected boolean canReplaceBlock(IBlockState parBlockState, IBlockState parBlockStateAbove)
    {
        if (parBlockState.getBlock() == Blocks.STONE) // ModBlocks.cloud)
        {
//             DEBUG
//            System.out.println("Can replace block");
            return true;
        }
        
//        // DEBUG
//        System.out.println("Cannot replace block of type = "+parBlockState.getBlock());
        return false;
    }
     
    @Override
    public void generate(World worldIn, int x, int z, ChunkPrimer primer)
    {
//        // DEBUG
//        System.out.println("MapGenCavesCloud generate()");
        
        super.generate(worldIn, x, z, primer);
    }

}
