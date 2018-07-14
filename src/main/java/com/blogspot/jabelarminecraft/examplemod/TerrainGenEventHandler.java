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

package com.blogspot.jabelarminecraft.examplemod;

import java.util.Random;

import com.blogspot.jabelarminecraft.examplemod.worldgen.WorldGenDungeonsModded;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TerrainGenEventHandler
{
    
    /**
     * On event.
     *
     * @param event the event
     */
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public  void onEvent(PopulateChunkEvent.Populate event)
    {
        if (event.getType() == EventType.DUNGEON)
        {
//            // DEBUG
//            System.out.println("Dungeon populate event at chunk start "+event.getChunkX()*16+" and "+event.getChunkZ()*16);
            
            ChunkGeneratorSettings settings = ChunkGeneratorSettings.Factory.jsonToFactory(event.getWorld().getWorldInfo().getGeneratorOptions()).build();
            Random rand = new Random(event.getWorld().getSeed());
            BlockPos blockpos = new BlockPos(event.getChunkX()*16, 0, event.getChunkZ()*16);
            
            for (int j2 = 0; j2 < settings.dungeonChance; ++j2)
            {
                int i3 = rand.nextInt(16) + 8;
                int l3 = rand.nextInt(256);
                int l1 = rand.nextInt(16) + 8;
                (new WorldGenDungeonsModded()).generate(event.getWorld(), rand, blockpos.add(i3, l3, l1));
            }
            
            event.setResult(Result.DENY);
        }
    }
}
