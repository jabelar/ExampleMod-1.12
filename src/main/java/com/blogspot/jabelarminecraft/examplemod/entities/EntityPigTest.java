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

import net.minecraft.entity.passive.EntityPig;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

// TODO: Auto-generated Javadoc
/**
 * @author jabelar
 *
 */
public class EntityPigTest extends EntityPig implements IModEntity
{

    /**
     * Instantiates a new entity pig test.
     *
     * @param worldIn
     *            the world in
     */
    public EntityPigTest(World worldIn)
    {
        super(worldIn);
        setSize(1.0F, 10.0F);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.blogspot.jabelarminecraft.examplemod.entities.IEntity#setupAI()
     */
    @Override
    public void setupAI()
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.blogspot.jabelarminecraft.examplemod.entities.IEntity#clearAITasks()
     */
    @Override
    public void clearAITasks()
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.blogspot.jabelarminecraft.examplemod.entities.IEntity#initSyncDataCompound()
     */
    @Override
    public void initSyncDataCompound()
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.blogspot.jabelarminecraft.examplemod.entities.IEntity#getSyncDataCompound()
     */
    @Override
    public NBTTagCompound getSyncDataCompound()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.blogspot.jabelarminecraft.examplemod.entities.IEntity#setSyncDataCompound(net.minecraft.nbt.NBTTagCompound)
     */
    @Override
    public void setSyncDataCompound(NBTTagCompound parCompound)
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.blogspot.jabelarminecraft.examplemod.entities.IEntity#sendEntitySyncPacket()
     */
    @Override
    public void sendEntitySyncPacket()
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.blogspot.jabelarminecraft.examplemod.entities.IEntity#setScaleFactor(float)
     */
    @Override
    public void setScaleFactor(float parScaleFactor)
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.blogspot.jabelarminecraft.examplemod.entities.IEntity#getScaleFactor()
     */
    @Override
    public float getScaleFactor()
    {
        // TODO Auto-generated method stub
        return 0;
    }
}
