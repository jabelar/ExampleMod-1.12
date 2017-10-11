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
package com.blogspot.jabelarminecraft.examplemod.worldsavedata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import com.blogspot.jabelarminecraft.examplemod.MainMod;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

// TODO: Auto-generated Javadoc
public class ProtectedAreaData extends WorldSavedData
{
    private static final String DATA_NAME = MainMod.MODID + "_data_aree";
    private List<ProtectedArea> listAreas = new ArrayList<ProtectedArea>();

    /**
     * Instantiates a new protected area data.
     */
    public ProtectedAreaData()
    {
        super(DATA_NAME);
    }

    /**
     * Instantiates a new protected area data.
     *
     * @param name
     *            the name
     */
    public ProtectedAreaData(String name)
    {
        super(name);
    }

    /**
     * Gets the world saved data instance associated to a given world.
     *
     * @param world
     *            the world
     * @return the data instance
     */
    public static ProtectedAreaData getDataInstance(World world)
    {
        MapStorage storage = world.getMapStorage();
        ProtectedAreaData instance = (ProtectedAreaData) storage.getOrLoadData(ProtectedAreaData.class, DATA_NAME);
        if (instance == null)
        {
            instance = new ProtectedAreaData();
            storage.setData(DATA_NAME, instance);
        }
        return instance;
    }

    /**
     * Adds the area to the list of protected areas.
     *
     * @param parArea
     *            the par area
     */
    public void addArea(ProtectedArea parArea)
    {
        listAreas.add(parArea);
        markDirty();
    }

    /**
     * Removes the area from the list of protected areas.
     *
     * @param parArea
     *            the par area
     */
    public void removeArea(ProtectedArea parArea)
    {
        listAreas.remove(parArea);
        markDirty();
    }

    /**
     * Clear the protected areas list.
     */
    public void clearAreas()
    {
        listAreas.clear();
        markDirty();
    }

    /**
     * Gets the area by name.
     *
     * @param parName
     *            the par name
     * @return the area by name
     */
    @Nullable
    public ProtectedArea getAreaByName(String parName)
    {
        Iterator<ProtectedArea> iterator = listAreas.iterator();
        while (iterator.hasNext())
        {
            ProtectedArea area = iterator.next();
            if (area.getName().equals(parName))
            {
                return area;
            }
        }

        return new ProtectedArea(parName);
    }

    /**
     * Adds the block to a given area.
     *
     * @param parName
     *            the par name
     * @param parPos
     *            the par pos
     */
    public void addBlockToArea(String parName, BlockPos parPos)
    {
        getAreaByName(parName).addBlock(parPos);
        markDirty();
    }

    /**
     * Removes the block from a given area.
     *
     * @param parName
     *            the par name
     * @param parPos
     *            the par pos
     */
    public void removeBlockFromArea(String parName, BlockPos parPos)
    {
        getAreaByName(parName).removeBlock(parPos);
        markDirty();
    }

    /**
     * Clear blocks from area.
     *
     * @param parName
     *            the par name
     */
    public void clearBlocksFromArea(String parName)
    {
        getAreaByName(parName).clearBlocks();
        markDirty();
    }

    /**
     * Checks if a block position is protected.
     *
     * @param parPos
     *            the par pos
     * @return true, if is block pos protected
     */
    public boolean isBlockPosProtected(BlockPos parPos)
    {
        Iterator<ProtectedArea> iteratorArea = listAreas.iterator();
        while (iteratorArea.hasNext())
        {
            ProtectedArea area = iteratorArea.next();
            if (area.getProtectedBlockList().contains(parPos))
            {
                return true;
            }
        }

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.minecraft.world.storage.WorldSavedData#readFromNBT(net.minecraft.nbt.NBTTagCompound)
     */
    // load
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        listAreas.clear();

        NBTTagList tagListAreas = nbt.getTagList("Protected Areas", 10); // 10 indicates a list of NBTTagCompound
        Iterator<NBTBase> iterator = tagListAreas.iterator();
        while (iterator.hasNext())
        {
            NBTTagCompound areaCompound = (NBTTagCompound) iterator.next();
            ProtectedArea area = new ProtectedArea(areaCompound.getString(areaCompound.getString("Area Name")));
            listAreas.add(area);
            NBTTagList tagListPos = areaCompound.getTagList("Block List", 10);

            Iterator<NBTBase> iterator2 = tagListPos.iterator();
            while (iterator2.hasNext())
            {
                NBTTagCompound posCompound = (NBTTagCompound) iterator2.next();
                BlockPos pos = new BlockPos(
                        posCompound.getInteger("x"),
                        posCompound.getInteger("y"),
                        posCompound.getInteger("z"));
                area.addBlock(pos);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.minecraft.world.storage.WorldSavedData#writeToNBT(net.minecraft.nbt.NBTTagCompound)
     */
    // save
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        NBTTagList tagList = new NBTTagList();

        // cycle through the list of areas
        Iterator<ProtectedArea> iteratorArea = listAreas.iterator();
        while (iteratorArea.hasNext())
        {
            NBTTagCompound tagCompound = new NBTTagCompound();
            ProtectedArea area = iteratorArea.next();
            tagCompound.setString("Area Name", area.getName());
            tagCompound.setTag("Block List", area.getBlockListTag());
            tagList.appendTag(tagCompound);
        }

        nbt.setTag("Protected Areas", tagList);
        return nbt;
    }
}
