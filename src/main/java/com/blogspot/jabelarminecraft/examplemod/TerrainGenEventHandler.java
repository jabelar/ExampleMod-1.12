/**
    Copyright (C) 2014 by jabelar

    This file is part of jabelar's Minecraft Forge modding examples; as such,
    you can redistribute it and/or modify it under the terms of the GNU
    General Public License as published by the Free Software Foundation,
    either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    For a copy of the GNU General Public License see <http://www.gnu.org/licenses/>.

	If you're interested in licensing the code under different terms you can
	contact the author at julian_abelar@hotmail.com 
*/

package com.blogspot.jabelarminecraft.examplemod;

import net.minecraftforge.event.terraingen.SaplingGrowTreeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class TerrainGenEventHandler 
{
	/*
	 * Terrain events 
	 */
	
//	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//	public void onEvent(BiomeEvent event)
//	{
//		
//	}
//	
//	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//	public void onEvent(BiomeColor event)
//	{
//		
//	}
//	
//	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//	public void onEvent(CreateDecorator event)
//	{
//		
//	}
//	
//	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//	public void onEvent(GetGrassColor event)
//	{
//		
//	}
//	
//	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//	public void onEvent(GetFoliageColor event)
//	{
//		
//	}
//	
//	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//	public void onEvent(GetVillageBlockID event)
//	{
//		
//	}
//	
////	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
////	public void onEvent(GetVillageBlockMeta event)
////	{
////		
////	}
//	
//	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//	public void onEvent(GetWaterColor event)
//	{
//		
//	}
//	
//	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//	public void onEvent(ChunkProviderEvent event)
//	{
//		
//	}
//	
//	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//	public void onEvent(InitNoiseField event)
//	{
//		
//	}
//	
//	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//	public void onEvent(ReplaceBiomeBlocks event)
//	{
//		
//	}
//	
//	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//	public void onEvent(DecorateBiomeEvent event)
//	{
//		
//	}
//	
//	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//	public void onEvent(DecorateBiomeEvent.Decorate event)
//	{
//		
//	}
//	
//	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//	public void onEvent(DecorateBiomeEvent.Post event)
//	{
//		
//	}
//	
//	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//	public void onEvent(DecorateBiomeEvent.Pre event)
//	{
//		
//	}
//	
//	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//	public void onEvent(InitMapGenEvent event)
//	{
//		
//	}
//	
//	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//	public void onEvent(PopulateChunkEvent.Pre event)
//	{
//
//	}
//	
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(SaplingGrowTreeEvent event)
	{
	    // DEBUG
	    System.out.println("Denying sapling growth");
		event.setResult(Result.DENY);
	}
//	
//	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//	public void onEvent(WorldTypeEvent event)
//	{
//		
//	}
//	
//	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//	public void onEvent(WorldTypeEvent.BiomeSize event)
//	{
//		
//	}
//	
//	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//	public void onEvent(WorldTypeEvent.InitBiomeGens event)
//	{
//		
//	}
}
