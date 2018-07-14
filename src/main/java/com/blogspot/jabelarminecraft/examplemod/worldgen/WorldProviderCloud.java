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

import com.blogspot.jabelarminecraft.examplemod.init.ModWorldGen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;

// TODO: Auto-generated Javadoc
public class WorldProviderCloud extends WorldProvider
{
    
    /* (non-Javadoc)
     * @see net.minecraft.world.WorldProvider#getDimensionType()
     */
    @Override
    public DimensionType getDimensionType()
    {
        return ModWorldGen.CLOUD_DIM_TYPE;
    }

    /**
     * Returns 'true' if in the "main surface world", but 'false' if in the Nether or End dimensions.
     *
     * @return true, if is surface world
     */
    @Override
    public boolean isSurfaceWorld()
    {
        return true;
    }

    /*======================================= Forge Start =========================================*/

    /* (non-Javadoc)
     * @see net.minecraft.world.WorldProvider#canDoLightning(net.minecraft.world.chunk.Chunk)
     */
    @Override
    public boolean canDoLightning(net.minecraft.world.chunk.Chunk chunk)
    {
        return true;
    }

    /* (non-Javadoc)
     * @see net.minecraft.world.WorldProvider#canDoRainSnowIce(net.minecraft.world.chunk.Chunk)
     */
    @Override
    public boolean canDoRainSnowIce(Chunk chunk)
    {
        return false;
    }
    
    /* (non-Javadoc)
     * @see net.minecraft.world.WorldProvider#canSnowAt(net.minecraft.util.math.BlockPos, boolean)
     */
    @Override
    public boolean canSnowAt(BlockPos pos, boolean checkLight)
    {
        return false; 
    }
}
