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

package com.blogspot.jabelarminecraft.examplemod.entities;

import net.minecraft.nbt.NBTTagCompound;

// TODO: Auto-generated Javadoc
public interface IEntity 
{
	
	/**
	 * Setup AI.
	 */
	// set up AI tasks
	void setupAI();
	
	/**
	 * Clear AI tasks.
	 */
	// use clear tasks for subclasses then build up their ai task list specifically
	void clearAITasks();
	
	/**
	 * Inits the sync data compound.
	 */
	// initialize the tag compound used for syncing custom entity data
	void initSyncDataCompound();
	
	/**
	 * Gets the sync data compound.
	 *
	 * @return the sync data compound
	 */
	NBTTagCompound getSyncDataCompound();
	
	/**
	 * Sets the sync data compound.
	 *
	 * @param parCompound the new sync data compound
	 */
	void setSyncDataCompound(NBTTagCompound parCompound);
	
	/**
	 * Send entity sync packet.
	 */
	// method to send sync of extended properties from server to clients
	void sendEntitySyncPacket();

	/**
	 * Sets the scale factor.
	 *
	 * @param parScaleFactor the new scale factor
	 */
	// common encapsulation methods
	void setScaleFactor(float parScaleFactor);
	
	/**
	 * Gets the scale factor.
	 *
	 * @return the scale factor
	 */
	float getScaleFactor();

}
