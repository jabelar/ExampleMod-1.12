package com.blogspot.jabelarminecraft.examplemod.gui;

import com.blogspot.jabelarminecraft.examplemod.MainMod;
import com.blogspot.jabelarminecraft.examplemod.containers.ContainerCompactor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
    { 
        // DEBUG
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

        // process those GUIs that have associated tile entities (i.e. for delayed results like forge)
        if (tileEntity != null)
        {
            if (ID == MainMod.GUI_ENUM.COMPACTOR.ordinal())
            {
                return new ContainerCompactor(player.inventory, (IInventory)tileEntity);
            }
        }
        // could process those GUIs that do not have associated entities (i.e. instant results) here

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

        // process those GUIs that have associated tile entities (i.e. for delayed results like forge)
        if (tileEntity != null)
        {
            if (ID == MainMod.GUI_ENUM.COMPACTOR.ordinal())
            {
                return new GuiCompactor(player.inventory, (IInventory)tileEntity);
            }
        }
        // could process those GUIs that do not have associated entities (i.e. instant results) here
        
       return null;
    }
}
