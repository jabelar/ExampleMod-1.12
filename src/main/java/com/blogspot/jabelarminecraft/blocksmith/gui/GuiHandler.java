package com.blogspot.jabelarminecraft.blocksmith.gui;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import com.blogspot.jabelarminecraft.blocksmith.BlockSmith;
import com.blogspot.jabelarminecraft.blocksmith.containers.ContainerCompactor;
import com.blogspot.jabelarminecraft.blocksmith.containers.ContainerDeconstructor;
import com.blogspot.jabelarminecraft.blocksmith.containers.ContainerForge;
import com.blogspot.jabelarminecraft.blocksmith.containers.ContainerGrinder;
import com.blogspot.jabelarminecraft.blocksmith.containers.ContainerTanningRack;

public class GuiHandler implements IGuiHandler
{

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
    { 
        // DEBUG
        System.out.println("GuiHandler getServerGuiElement() with ID = "+ID+" and deconstructor is ID "+BlockSmith.GUI_ENUM.DECONSTRUCTOR.ordinal());
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

        if (tileEntity != null)
        {
            if (ID == BlockSmith.GUI_ENUM.GRINDER.ordinal())
            {
                return new ContainerGrinder(player.inventory, (IInventory)tileEntity);
            }
            if (ID == BlockSmith.GUI_ENUM.COMPACTOR.ordinal())
            {
                return new ContainerCompactor(player.inventory, (IInventory)tileEntity);
            }
            if (ID == BlockSmith.GUI_ENUM.TANNING_RACK.ordinal())
            {
                return new ContainerTanningRack(player.inventory, (IInventory)tileEntity);
            }
            if (ID == BlockSmith.GUI_ENUM.FORGE.ordinal())
            {
                return new ContainerForge(player.inventory, (IInventory)tileEntity);
            }
        }
        if (ID == BlockSmith.GUI_ENUM.DECONSTRUCTOR.ordinal())
        {
            return new ContainerDeconstructor(player.inventory, world, x, y, z);
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        // DEBUG
        System.out.println("GuiHandler getClientGuiElement() with ID = "+ID);
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

        if (tileEntity != null)
        {
            if (ID == BlockSmith.GUI_ENUM.GRINDER.ordinal())
            {
                return new GuiGrinder(player.inventory, (IInventory)tileEntity);
            }
            if (ID == BlockSmith.GUI_ENUM.COMPACTOR.ordinal())
            {
                return new GuiCompactor(player.inventory, (IInventory)tileEntity);
            }
            if (ID == BlockSmith.GUI_ENUM.TANNING_RACK.ordinal())
            {
                return new GuiTanningRack(player.inventory, (IInventory)tileEntity);
            }
            if (ID == BlockSmith.GUI_ENUM.FORGE.ordinal())
            {
                return new GuiForge(player.inventory, (IInventory)tileEntity);
            }
       }
        if (ID == BlockSmith.GUI_ENUM.DECONSTRUCTOR.ordinal())
        {
            return new GuiDeconstructor(player.inventory, world, I18n.format("tile.deconstructor.name"), x, y, z);
        }
        return null;
    }
}
