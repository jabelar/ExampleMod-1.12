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

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

// TODO: Auto-generated Javadoc
public class WorldData extends WorldSavedData 
{

	private static final String IDENTIFIER = MainMod.MODID;
	
//	private boolean hasCastleSpawned = false;
//	private boolean familyCowHasGivenLead = false;
	
	/**
 * Instantiates a new world data.
 */
public WorldData() 
	{
		this(IDENTIFIER);
	}
	
	/**
	 * Instantiates a new world data.
	 *
	 * @param parIdentifier the par identifier
	 */
	public WorldData(String parIdentifier) 
	{
		super(parIdentifier);
		markDirty();
	}

	/* (non-Javadoc)
	 * @see net.minecraft.world.storage.WorldSavedData#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		// DEBUG
		System.out.println("WorldData readFromNBT");
		
//		hasCastleSpawned = nbt.getBoolean("hasCastleSpawned");
//		familyCowHasGivenLead = nbt.getBoolean("familyCowHasGivenLead");
	}

	/* (non-Javadoc)
	 * @see net.minecraft.world.storage.WorldSavedData#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) 
	{
		// DEBUG
		System.out.println("MagicBeansWorldData writeToNBT");
		return nbt;
		
//		nbt.setBoolean("hasCastleSpawned", hasCastleSpawned);
//		nbt.setBoolean("familyCowHasGivenLead", familyCowHasGivenLead);
	}
	
//	public boolean getHasCastleSpwaned() 
//	{
//		return hasCastleSpawned;
//	}
//	
//	public void setHasCastleSpawned(boolean parHasCastleSpawned) 
//	{
//		// DEBUG
//		System.out.println("World Data setHasCastleSpawned = "+parHasCastleSpawned);
//		if (!hasCastleSpawned) 
//		{
//			hasCastleSpawned = true;
//			markDirty();
//			// new PacketWorldData(this).sendToAll(); shouldn't need to send packet as this field is only used on server side
//		}
//	}
//	
//	public boolean getFamilyCowHasGivenLead() 
//	{
//		return familyCowHasGivenLead;
//	}
//	
//	public void setFamilyCowHasGivenLead(boolean parFamilyCowHasGivenLead) 
//	{
//		// DEBUG
//		System.out.println("World Data familyCowHasGivenLead = "+parFamilyCowHasGivenLead);
//		if (!familyCowHasGivenLead) 
//		{
//			familyCowHasGivenLead = true;
//			markDirty();
//			// new PacketWorldData(this).sendToAll(); shouldn't need to send packet as this field is only used on server side
//		}
//	}
		
	/**
 * Gets the.
 *
 * @param world the world
 * @return the world data
 */
public static WorldData get(World world) 
	{
		WorldData data = (WorldData)world.loadData(WorldData.class, IDENTIFIER);
		if (data == null) 
		{
			// DEBUG
			System.out.println("WorldData didn't exist so creating it");
			
			data = new WorldData();
			world.setData(IDENTIFIER, data);
		}
		return data;
	}
}