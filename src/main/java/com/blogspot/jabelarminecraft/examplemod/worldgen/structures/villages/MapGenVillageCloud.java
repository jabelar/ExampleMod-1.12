package com.blogspot.jabelarminecraft.examplemod.worldgen.structures.villages;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.blogspot.jabelarminecraft.examplemod.init.ModBiomes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraft.world.gen.structure.StructureVillagePieces;

public class MapGenVillageCloud extends MapGenStructure
{
    /** A list of all the biomes villages can spawn in. */
    public static List<Biome> VILLAGE_SPAWN_BIOMES = Arrays.<Biome>asList(ModBiomes.cloud);
    /** None */
    private int size;
    private int distance;
    private final int minTownSeparation;

    public MapGenVillageCloud()
    {
        distance = 32;
        minTownSeparation = 8;
    }

    public MapGenVillageCloud(Map<String, String> map)
    {
        this();

        // Initialize the village entry map
        for (Entry<String, String> entry : map.entrySet())
        {
            if (entry.getKey().equals("size"))
            {
                size = MathHelper.getInt(entry.getValue(), size, 0);
            }
            else if (entry.getKey().equals("distance"))
            {
                distance = MathHelper.getInt(entry.getValue(), distance, 9);
            }
        }
    }

    @Override
    public String getStructureName()
    {
        return "Cloud Village";
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)
    {
        int unadjustedX = chunkX;
        int unadjustedZ = chunkZ;

        if (chunkX < 0)
        {
            chunkX -= distance - 1;
        }

        if (chunkZ < 0)
        {
            chunkZ -= distance - 1;
        }

        // randomize relative positions of village candidate sites
        int candidateX = chunkX / distance;
        int candidateZ = chunkZ / distance;
        Random random = world.setRandomSeed(candidateX, candidateZ, 10387312);
        candidateX = candidateX * distance;
        candidateZ = candidateZ * distance;
        candidateX = candidateX + random.nextInt(distance - 8);
        candidateZ = candidateZ + random.nextInt(distance - 8);

        if (unadjustedX == candidateX && unadjustedZ == candidateZ)
        {
            // DEBUG
            System.out.println("Is biome viable for village = "+world.getBiomeProvider().areBiomesViable(unadjustedX * 16 + 8, unadjustedZ * 16 + 8, 0, VILLAGE_SPAWN_BIOMES));
            return world.getBiomeProvider().areBiomesViable(unadjustedX * 16 + 8, unadjustedZ * 16 + 8, 0, VILLAGE_SPAWN_BIOMES);
        }

        return false;
    }

    @Override
    public BlockPos getNearestStructurePos(World worldIn, BlockPos pos, boolean findUnexplored)
    {
        world = worldIn;
        return findNearestStructurePosBySpacing(worldIn, this, pos, distance, 8, 10387312, false, 100, findUnexplored);
    }

    @Override
    protected StructureStart getStructureStart(int chunkX, int chunkZ)
    {
        return new MapGenVillageCloud.Start(world, rand, chunkX, chunkZ, size);
    }

    public static class Start extends StructureStart
        {
            /** well ... thats what it does */
            private boolean hasMoreThanTwoComponents;

            public Start()
            {
            }

            public Start(World worldIn, Random rand, int x, int z, int size)
            {
                super(x, z);
                List<StructureVillagePieces.PieceWeight> list = StructureVillagePieces.getStructureVillageWeightedPieceList(rand, size);
                StructureVillagePieces.Start structurevillagepieces$start = new StructureVillagePieces.Start(worldIn.getBiomeProvider(), 0, rand, (x << 4) + 2, (z << 4) + 2, list, size);
                components.add(structurevillagepieces$start);
                structurevillagepieces$start.buildComponent(structurevillagepieces$start, components, rand);
                List<StructureComponent> list1 = structurevillagepieces$start.pendingRoads;
                List<StructureComponent> list2 = structurevillagepieces$start.pendingHouses;

                while (!list1.isEmpty() || !list2.isEmpty())
                {
                    if (list1.isEmpty())
                    {
                        int i = rand.nextInt(list2.size());
                        StructureComponent structurecomponent = list2.remove(i);
                        structurecomponent.buildComponent(structurevillagepieces$start, components, rand);
                    }
                    else
                    {
                        int j = rand.nextInt(list1.size());
                        StructureComponent structurecomponent2 = list1.remove(j);
                        structurecomponent2.buildComponent(structurevillagepieces$start, components, rand);
                    }
                }

                updateBoundingBox();
                int nonRoadComponentCount = 0;

                for (StructureComponent structurecomponent1 : components)
                {
                    if (!(structurecomponent1 instanceof StructureVillagePieces.Road))
                    {
                        ++nonRoadComponentCount;
                    }
                }

                hasMoreThanTwoComponents = nonRoadComponentCount > 2;
            }

            /**
             * currently only defined for Villages, returns true if Village has more than 2 non-road components
             */
            @Override
            public boolean isSizeableStructure()
            {
                return hasMoreThanTwoComponents;
            }

            @Override
            public void writeToNBT(NBTTagCompound tagCompound)
            {
                super.writeToNBT(tagCompound);
                tagCompound.setBoolean("Valid", hasMoreThanTwoComponents);
            }

            @Override
            public void readFromNBT(NBTTagCompound tagCompound)
            {
                super.readFromNBT(tagCompound);
                hasMoreThanTwoComponents = tagCompound.getBoolean("Valid");
            }
        }
}