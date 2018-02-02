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
package com.blogspot.jabelarminecraft.examplemod.worldgen;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenRavine;

// TODO: Auto-generated Javadoc
public class MapGenRavineCloud extends MapGenRavine
{
    
    /* (non-Javadoc)
     * @see net.minecraft.world.gen.MapGenRavine#addTunnel(long, int, int, net.minecraft.world.chunk.ChunkPrimer, double, double, double, float, float, float, int, int, double)
     */
    @Override
    protected void addTunnel(long p_180707_1_, int p_180707_3_, int p_180707_4_, ChunkPrimer p_180707_5_, double p_180707_6_, double p_180707_8_, double p_180707_10_, float p_180707_12_, float p_180707_13_, float p_180707_14_, int p_180707_15_, int p_180707_16_, double p_180707_17_)
    {
//        // DEBUG
//        System.out.println("MapGenRavineCloud addTunnel()");
        
        super.addTunnel(p_180707_1_, p_180707_3_, p_180707_4_, p_180707_5_, p_180707_6_, p_180707_8_, p_180707_10_, p_180707_12_, p_180707_13_, p_180707_14_, p_180707_15_, p_180707_16_, p_180707_17_);
    }
    
    /* (non-Javadoc)
     * @see net.minecraft.world.gen.MapGenBase#generate(net.minecraft.world.World, int, int, net.minecraft.world.chunk.ChunkPrimer)
     */
    @Override
    public void generate(World worldIn, int x, int z, ChunkPrimer primer)
    {
//        // DEBUG
//        System.out.println("MapGenRavineCloud generate()");
        
        super.generate(worldIn, x, z, primer);
    }
}
