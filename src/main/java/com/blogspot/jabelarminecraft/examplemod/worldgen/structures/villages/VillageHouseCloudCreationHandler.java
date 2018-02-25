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

public class VillageHouseCloudCreationHandler implements VillagerRegistry.IVillageCreationHandler
{
    @Override
    public PieceWeight getVillagePieceWeight(Random parRandom, int parType)
    {
        // DEBUG
        System.out.println("Getting village house cloud piece weight");
        
        return new PieceWeight(getComponentClass(), 5, 3);
    }

    @Override
    public Class<? extends Village> getComponentClass()
    {
        return VillageHouseCloud.class;
    }

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
