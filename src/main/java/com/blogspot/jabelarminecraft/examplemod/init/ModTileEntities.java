package com.blogspot.jabelarminecraft.examplemod.init;

import com.blogspot.jabelarminecraft.examplemod.tileentities.TileEntityCompactor;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModTileEntities
{
    /**
     * Registers tile entities.
     */
    public static void registerTileEntities()
    {
        // DEBUG
        System.out.println("Registering tile entities");
        GameRegistry.registerTileEntity(TileEntityCompactor.class, "tileEntityCompactor");
    }

}
