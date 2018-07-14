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
package com.blogspot.jabelarminecraft.examplemod.worldgen.structures.villages;

import java.util.List;
import java.util.Random;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces.PieceWeight;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;
import net.minecraft.world.gen.structure.StructureVillagePieces.Village;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

// TODO: Auto-generated Javadoc
public class VillageHouseCloudCreationHandler implements VillagerRegistry.IVillageCreationHandler
{
    
    /* (non-Javadoc)
     * @see net.minecraftforge.fml.common.registry.VillagerRegistry.IVillageCreationHandler#getVillagePieceWeight(java.util.Random, int)
     */
    @Override
    public PieceWeight getVillagePieceWeight(Random parRandom, int parType)
    {
        // DEBUG
        System.out.println("Getting village house cloud piece weight");
        
        return new PieceWeight(getComponentClass(), 5, 3);
    }

    /* (non-Javadoc)
     * @see net.minecraftforge.fml.common.registry.VillagerRegistry.IVillageCreationHandler#getComponentClass()
     */
    @Override
    public Class<? extends Village> getComponentClass()
    {
        return VillageHouseCloud.class;
    }

    /* (non-Javadoc)
     * @see net.minecraftforge.fml.common.registry.VillagerRegistry.IVillageCreationHandler#buildComponent(net.minecraft.world.gen.structure.StructureVillagePieces.PieceWeight, net.minecraft.world.gen.structure.StructureVillagePieces.Start, java.util.List, java.util.Random, int, int, int, net.minecraft.util.EnumFacing, int)
     */
    @Override
    public Village buildComponent(
            PieceWeight parPieceWeight, 
            Start parStart,
            List<StructureComponent> parPiecesList, 
            Random parRand, 
            int parMinX, int parMinY, int parMinZ,
            EnumFacing parFacing, 
            int parType
            )
    {
        // DEBUG
        System.out.println("Village House Cloud buildComponent() at "+parMinX+", "+parMinY+", "+parMinZ);
        
        StructureBoundingBox structBB = StructureBoundingBox.getComponentToAddBoundingBox(parMinX, parMinY, parMinZ, 0, 0, 0, 9, 7, 12, parFacing);
        return new VillageHouseCloud(parStart, parType, parRand, structBB, parFacing);
    }

}
