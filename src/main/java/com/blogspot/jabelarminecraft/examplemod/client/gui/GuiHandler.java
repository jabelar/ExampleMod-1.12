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
package com.blogspot.jabelarminecraft.examplemod.client.gui;

import com.blogspot.jabelarminecraft.examplemod.containers.ContainerCompactor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

// TODO: Auto-generated Javadoc
public class GuiHandler implements IGuiHandler
{
    // enumerate guis
    public enum GUI_ENUM
    {
        COMPACTOR
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.minecraftforge.fml.common.network.IGuiHandler#getServerGuiElement(int, net.minecraft.entity.player.EntityPlayer, net.minecraft.world.World, int, int, int)
     */
    // On server side you return the container (not the GUI!)
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        // DEBUG
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

        // process those GUIs that have associated tile entities (i.e. for delayed results like forge)
        if (tileEntity != null)
        {
            if (ID == GUI_ENUM.COMPACTOR.ordinal())
            {
                // DEBUG
                System.out.println("GUI handler server element creating ContainerCompactor");
                return new ContainerCompactor(player.inventory, (IInventory) tileEntity);
            }
        }
        // could process those GUIs that do not have associated entities (i.e. instant results) here

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.minecraftforge.fml.common.network.IGuiHandler#getClientGuiElement(int, net.minecraft.entity.player.EntityPlayer, net.minecraft.world.World, int, int, int)
     */
    // On the client side you return the GUI (not the container!)
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

        // process those GUIs that have associated tile entities (i.e. for delayed results like forge)
        if (tileEntity != null)
        {
            if (ID == GUI_ENUM.COMPACTOR.ordinal())
            {
                // DEBUG
                System.out.println("GUI handler client element creating GUICompactor");
                return new GuiCompactor(player.inventory, (IInventory) tileEntity);
            }
        }
        // could process those GUIs that do not have associated entities (i.e. instant results) here

        return null;
    }
}
