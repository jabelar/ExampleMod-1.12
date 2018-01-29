package com.blogspot.jabelarminecraft.examplemod.worldgen;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.StructureOceanMonument;
import net.minecraftforge.event.terraingen.InitNoiseGensEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

public class ChunkGeneratorCloud implements IChunkGenerator
{
    protected static final IBlockState BASE_BLOCK = Blocks.STONE.getDefaultState(); // ModBlocks.cloud.getDefaultState();
    private IBlockState oceanBlock = Blocks.WATER.getDefaultState(); // not final to support lava oceans option
    private final Random rand;
    private NoiseGeneratorOctaves minLimitPerlinNoise;
    private NoiseGeneratorOctaves maxLimitPerlinNoise;
    private NoiseGeneratorOctaves mainPerlinNoise;
    private NoiseGeneratorPerlin surfaceNoise;
    public NoiseGeneratorOctaves scaleNoise;
    public NoiseGeneratorOctaves depthNoise;
    public NoiseGeneratorOctaves forestNoise;
    private final World world;
    private final boolean mapFeaturesEnabled;
    private final WorldType terrainType;
    private final double[] heightMap;
    private final float[] biomeWeights;
    private ChunkGeneratorSettings settings;
    private double[] depthBuffer = new double[256];
    private MapGenBase caveGenerator = new MapGenCavesCloud();
    private MapGenStronghold strongholdGenerator = new MapGenStronghold();
    private MapGenVillage villageGenerator = new MapGenVillage();
    private MapGenMineshaft mineshaftGenerator = new MapGenMineshaft();
    private MapGenScatteredFeature scatteredFeatureGenerator = new MapGenScatteredFeature();
    private MapGenBase ravineGenerator = new MapGenRavineCloud();
    private StructureOceanMonument oceanMonumentGenerator = new StructureOceanMonument();
    private Biome[] biomesForGeneration;
    double[] mainNoiseRegion;
    double[] minLimitRegion;
    double[] maxLimitRegion;
    double[] depthRegion;

    public ChunkGeneratorCloud(World worldIn, long seed, boolean mapFeaturesEnabledIn, String generatorOptions)
    {
        // DEBUG
        System.out.println("Constructing ChunkGeneratorCloud with seed = "+seed+" and map features enabled = "+mapFeaturesEnabledIn+" and generator options = "+generatorOptions);
        
//        caveGenerator = TerrainGen.getModdedMapGen(caveGenerator, InitMapGenEvent.EventType.CAVE);
//        strongholdGenerator = (MapGenStronghold) TerrainGen.getModdedMapGen(strongholdGenerator, InitMapGenEvent.EventType.STRONGHOLD);
//        villageGenerator = (MapGenVillage) TerrainGen.getModdedMapGen(villageGenerator, InitMapGenEvent.EventType.VILLAGE);
//        mineshaftGenerator = (MapGenMineshaft) TerrainGen.getModdedMapGen(mineshaftGenerator, InitMapGenEvent.EventType.MINESHAFT);
//        scatteredFeatureGenerator = (MapGenScatteredFeature) TerrainGen.getModdedMapGen(scatteredFeatureGenerator, InitMapGenEvent.EventType.SCATTERED_FEATURE);
//        ravineGenerator = TerrainGen.getModdedMapGen(ravineGenerator, InitMapGenEvent.EventType.RAVINE);
//        oceanMonumentGenerator = (StructureOceanMonument) TerrainGen.getModdedMapGen(oceanMonumentGenerator, InitMapGenEvent.EventType.OCEAN_MONUMENT);

        world = worldIn;
        mapFeaturesEnabled = mapFeaturesEnabledIn;
        terrainType = worldIn.getWorldInfo().getTerrainType();
        rand = new Random(seed);
        minLimitPerlinNoise = new NoiseGeneratorOctaves(rand, 16);
        maxLimitPerlinNoise = new NoiseGeneratorOctaves(rand, 16);
        mainPerlinNoise = new NoiseGeneratorOctaves(rand, 8);
        surfaceNoise = new NoiseGeneratorPerlin(rand, 4);
        scaleNoise = new NoiseGeneratorOctaves(rand, 10);
        depthNoise = new NoiseGeneratorOctaves(rand, 16);
        forestNoise = new NoiseGeneratorOctaves(rand, 8);
        heightMap = new double[825];
        biomeWeights = new float[25];

        for (int i = -2; i <= 2; ++i)
        {
            for (int j = -2; j <= 2; ++j)
            {
                float f = 10.0F / MathHelper.sqrt(i * i + j * j + 0.2F);
                biomeWeights[i + 2 + (j + 2) * 5] = f;
            }
        }

        if (generatorOptions != null)
        {
            settings = ChunkGeneratorSettings.Factory.jsonToFactory(generatorOptions).build();
            oceanBlock = settings.useLavaOceans ? Blocks.LAVA.getDefaultState() : Blocks.WATER.getDefaultState();
            worldIn.setSeaLevel(settings.seaLevel);
        }

        InitNoiseGensEvent.ContextOverworld ctx = new InitNoiseGensEvent.ContextOverworld(
                minLimitPerlinNoise, maxLimitPerlinNoise, mainPerlinNoise, surfaceNoise, scaleNoise, depthNoise, forestNoise);
        ctx = TerrainGen.getModdedNoiseGenerators(worldIn, rand, ctx);
        minLimitPerlinNoise = ctx.getLPerlin1();
        maxLimitPerlinNoise = ctx.getLPerlin2();
        mainPerlinNoise = ctx.getPerlin();
        surfaceNoise = ctx.getHeight();
        scaleNoise = ctx.getScale();
        depthNoise = ctx.getDepth();
        forestNoise = ctx.getForest();
    }

    public void setBlocksInChunk(int parX, int parZ, ChunkPrimer parChunkPrimer)
    {
//        // DEBUG
//        System.out.println("Setting blocks in chunk at x = "+x+" and z = "+z);
        for (int y = 0; y < settings.seaLevel; ++y)
        {
            for (int x = 0; x < 16; ++x)
            {
                for (int z = 0; z < 16; ++z)
                {
                    parChunkPrimer.setBlockState(x, y, z, BASE_BLOCK);
                }
            }
        }     
    }

    public void replaceBiomeBlocks(int x, int z, ChunkPrimer primer, Biome[] biomesIn)
    {
//        // DEBUG
//        System.out.println("Replacing biome blocks");
        
        if (!net.minecraftforge.event.ForgeEventFactory.onReplaceBiomeBlocks(this, x, z, primer, world))
            return;
//        depthBuffer = surfaceNoise.getRegion(depthBuffer, x * 16, z * 16, 16, 16, 0.0625D, 0.0625D, 1.0D);
//
//        for (int i = 0; i < 16; ++i)
//        {
//            for (int j = 0; j < 16; ++j)
//            {
//                Biome biome = biomesIn[j + i * 16];
//                
////                // DEBUG
////                System.out.println("Biome = "+biome.getBiomeName());
//                
//                biome.genTerrainBlocks(world, rand, primer, x * 16 + i, z * 16 + j, depthBuffer[j + i * 16]);
//            }
//        }
    }

    /**
     * Generates the chunk at the specified position, from scratch
     */
    @Override
    public Chunk generateChunk(int x, int z)
    {
        // DEBUG
        System.out.println("Generating chunk at x = "+x+" and z = "+z);
        
        rand.setSeed(x * 341873128712L + z * 132897987541L);
        ChunkPrimer chunkprimer = new ChunkPrimer();
        
        setBlocksInChunk(x, z, chunkprimer);
        biomesForGeneration = world.getBiomeProvider().getBiomes(biomesForGeneration, x * 16, z * 16, 16, 16);
        replaceBiomeBlocks(x, z, chunkprimer, biomesForGeneration);

//        if (settings.useCaves)
//        {
//            caveGenerator.generate(world, x, z, chunkprimer);
//        }
//
//        if (settings.useRavines)
//        {
//            ravineGenerator.generate(world, x, z, chunkprimer);
//        }
//
//        if (mapFeaturesEnabled)
//        {
//            if (settings.useMineShafts)
//            {
//                mineshaftGenerator.generate(world, x, z, chunkprimer);
//            }
//
//            if (settings.useVillages)
//            {
//                villageGenerator.generate(world, x, z, chunkprimer);
//            }
//
//            if (settings.useStrongholds)
//            {
//                strongholdGenerator.generate(world, x, z, chunkprimer);
//            }
//
//            if (settings.useTemples)
//            {
//                scatteredFeatureGenerator.generate(world, x, z, chunkprimer);
//            }
//
//            if (settings.useMonuments)
//            {
//                oceanMonumentGenerator.generate(world, x, z, chunkprimer);
//            }
//        }

        Chunk chunk = new Chunk(world, chunkprimer, x, z);
        byte[] abyte = chunk.getBiomeArray();

        for (int i = 0; i < abyte.length; ++i)
        {
            abyte[i] = (byte) Biome.getIdForBiome(biomesForGeneration[i]);
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    private void generateHeightmap(int xTimes4, int zTimes4)
    {
        depthRegion = depthNoise.generateNoiseOctaves(depthRegion, xTimes4, zTimes4, 5, 5, settings.depthNoiseScaleX,
                settings.depthNoiseScaleZ, settings.depthNoiseScaleExponent);
        float coordScale = settings.coordinateScale;
        float heightScale = settings.heightScale;
        mainNoiseRegion = mainPerlinNoise.generateNoiseOctaves(mainNoiseRegion, xTimes4, 0, zTimes4, 5, 33, 5,
                coordScale / settings.mainNoiseScaleX, heightScale / settings.mainNoiseScaleY, coordScale / settings.mainNoiseScaleZ);
        minLimitRegion = minLimitPerlinNoise.generateNoiseOctaves(minLimitRegion, xTimes4, 0, zTimes4, 5, 33, 5, coordScale,
                heightScale, coordScale);
        maxLimitRegion = maxLimitPerlinNoise.generateNoiseOctaves(maxLimitRegion, xTimes4, 0, zTimes4, 5, 33, 5, coordScale,
                heightScale, coordScale);
        int i = 0;
        int j = 0;

        for (int k = 0; k < 5; ++k)
        {
            for (int l = 0; l < 5; ++l)
            {
                float f2 = 0.0F;
                float f3 = 0.0F;
                float f4 = 0.0F;
                Biome biome = biomesForGeneration[k + 2 + (l + 2) * 10];
                
//                // DEBUG
//                System.out.println("Biome = "+biome.getBiomeName());

                for (int j1 = -2; j1 <= 2; ++j1)
                {
                    for (int k1 = -2; k1 <= 2; ++k1)
                    {
                        Biome biome1 = biomesForGeneration[k + j1 + 2 + (l + k1 + 2) * 10];

//                        // DEBUG
//                        System.out.println("Biome1 = "+biome1.getBiomeName());

                        float f5 = settings.biomeDepthOffSet + biome1.getBaseHeight() * settings.biomeDepthWeight;
                        float f6 = settings.biomeScaleOffset + biome1.getHeightVariation() * settings.biomeScaleWeight;

                        float f7 = biomeWeights[j1 + 2 + (k1 + 2) * 5] / (f5 + 2.0F);

                        if (biome1.getBaseHeight() > biome.getBaseHeight())
                        {
                            f7 /= 2.0F;
                        }

                        f2 += f6 * f7;
                        f3 += f5 * f7;
                        f4 += f7;
                    }
                }

                f2 = f2 / f4;
                f3 = f3 / f4;
                f2 = f2 * 0.9F + 0.1F;
                f3 = (f3 * 4.0F - 1.0F) / 8.0F;
                double d7 = depthRegion[j] / 8000.0D;

                if (d7 < 0.0D)
                {
                    d7 = -d7 * 0.3D;
                }

                d7 = d7 * 3.0D - 2.0D;

                if (d7 < 0.0D)
                {
                    d7 = d7 / 2.0D;

                    if (d7 < -1.0D)
                    {
                        d7 = -1.0D;
                    }

                    d7 = d7 / 1.4D;
                    d7 = d7 / 2.0D;
                }
                else
                {
                    if (d7 > 1.0D)
                    {
                        d7 = 1.0D;
                    }

                    d7 = d7 / 8.0D;
                }

                ++j;
                double d8 = f3;
                double d9 = f2;
                d8 = d8 + d7 * 0.2D;
                d8 = d8 * settings.baseSize / 8.0D;
                double d0 = settings.baseSize + d8 * 4.0D;

                for (int l1 = 0; l1 < 33; ++l1)
                {
                    double d1 = (l1 - d0) * settings.stretchY * 128.0D / 256.0D / d9;

                    if (d1 < 0.0D)
                    {
                        d1 *= 4.0D;
                    }

                    double d2 = minLimitRegion[i] / settings.lowerLimitScale;
                    double d3 = maxLimitRegion[i] / settings.upperLimitScale;
                    double d4 = (mainNoiseRegion[i] / 10.0D + 1.0D) / 2.0D;
                    double d5 = MathHelper.clampedLerp(d2, d3, d4) - d1;

                    if (l1 > 29)
                    {
                        double d6 = (l1 - 29) / 3.0F;
                        d5 = d5 * (1.0D - d6) + -10.0D * d6;
                    }

                    heightMap[i] = d5;
                    ++i;
                }
            }
        }
    }

    /**
     * Generate initial structures in this chunk, e.g. mineshafts, temples, lakes, and dungeons
     * 
     * @param x Chunk x coordinate
     * @param z Chunk z coordinate
     */
    @Override
    public void populate(int x, int z)
    {
//        // DEBUG
//        System.out.println("Populating chunk at x = "+x+" and z = "+z);
        
        BlockFalling.fallInstantly = true;
        int i = x * 16;
        int j = z * 16;
        BlockPos blockpos = new BlockPos(i, 0, j);
//        Biome biome = world.getBiome(blockpos.add(16, 0, 16));
        Biome biome = world.getBiomeProvider().getBiome(blockpos.add(16, 0, 16));
        
//        // DEBUG
//        System.out.println("Biome = "+biome.getBiomeName()+" and block is loaded = "+world.isBlockLoaded(blockpos));
        
        rand.setSeed(world.getSeed());
        long k = rand.nextLong() / 2L * 2L + 1L;
        long l = rand.nextLong() / 2L * 2L + 1L;
        rand.setSeed(x * k + z * l ^ world.getSeed());
        boolean flag = false;
//        ChunkPos chunkpos = new ChunkPos(x, z);

        net.minecraftforge.event.ForgeEventFactory.onChunkPopulate(true, this, world, rand, x, z, flag);

//        if (mapFeaturesEnabled)
//        {
//            if (settings.useMineShafts)
//            {
//                mineshaftGenerator.generateStructure(world, rand, chunkpos);
//            }
//
//            if (settings.useVillages)
//            {
//                flag = villageGenerator.generateStructure(world, rand, chunkpos);
//            }
//
//            if (settings.useStrongholds)
//            {
//                strongholdGenerator.generateStructure(world, rand, chunkpos);
//            }
//
//            if (settings.useTemples)
//            {
//                scatteredFeatureGenerator.generateStructure(world, rand, chunkpos);
//            }
//
//            if (settings.useMonuments)
//            {
//                oceanMonumentGenerator.generateStructure(world, rand, chunkpos);
//            }
//
//        }

//        if (settings.useWaterLakes && !flag && rand.nextInt(settings.waterLakeChance) == 0)
//            if (TerrainGen.populate(this, world, rand, x, z, flag, PopulateChunkEvent.Populate.EventType.LAKE))
//            {
//                int i1 = rand.nextInt(16) + 8;
//                int j1 = rand.nextInt(256);
//                int k1 = rand.nextInt(16) + 8;
//                (new WorldGenLakes(Blocks.WATER)).generate(world, rand, blockpos.add(i1, j1, k1));
//            }
//
//        if (!flag && rand.nextInt(settings.lavaLakeChance / 10) == 0 && settings.useLavaLakes)
//            if (TerrainGen.populate(this, world, rand, x, z, flag,
//                    PopulateChunkEvent.Populate.EventType.LAVA))
//            {
//                int i2 = rand.nextInt(16) + 8;
//                int l2 = rand.nextInt(rand.nextInt(248) + 8);
//                int k3 = rand.nextInt(16) + 8;
//
//                if (l2 < world.getSeaLevel() || rand.nextInt(settings.lavaLakeChance / 8) == 0)
//                {
//                    (new WorldGenLakes(Blocks.LAVA)).generate(world, rand, blockpos.add(i2, l2, k3));
//                }
//            }
//
//        if (settings.useDungeons)
//            if (TerrainGen.populate(this, world, rand, x, z, flag,
//                    PopulateChunkEvent.Populate.EventType.DUNGEON))
//            {
//                for (int j2 = 0; j2 < settings.dungeonChance; ++j2)
//                {
//                    int i3 = rand.nextInt(16) + 8;
//                    int l3 = rand.nextInt(256);
//                    int l1 = rand.nextInt(16) + 8;
//                    (new WorldGenDungeons()).generate(world, rand, blockpos.add(i3, l3, l1));
//                }
//            }

        biome.decorate(world, rand, new BlockPos(i, 0, j));
        if (TerrainGen.populate(this, world, rand, x, z, flag,
                PopulateChunkEvent.Populate.EventType.ANIMALS))
            WorldEntitySpawner.performWorldGenSpawning(world, biome, i + 8, j + 8, 16, 16, rand);
        blockpos = blockpos.add(8, 0, 8);

//        if (TerrainGen.populate(this, world, rand, x, z, flag,
//                PopulateChunkEvent.Populate.EventType.ICE))
//        {
//            for (int k2 = 0; k2 < 16; ++k2)
//            {
//                for (int j3 = 0; j3 < 16; ++j3)
//                {
//                    BlockPos blockpos1 = world.getPrecipitationHeight(blockpos.add(k2, 0, j3));
//                    BlockPos blockpos2 = blockpos1.down();
//
//                    if (world.canBlockFreezeWater(blockpos2))
//                    {
//                        world.setBlockState(blockpos2, Blocks.ICE.getDefaultState(), 2);
//                    }
//
//                    if (world.canSnowAt(blockpos1, true))
//                    {
//                        world.setBlockState(blockpos1, Blocks.SNOW_LAYER.getDefaultState(), 2);
//                    }
//                }
//            }
//        } // Forge: End ICE

        net.minecraftforge.event.ForgeEventFactory.onChunkPopulate(false, this, world, rand, x, z, flag);

        BlockFalling.fallInstantly = false;
    }

    /**
     * Called to generate additional structures after initial worldgen, used by ocean monuments
     */
    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z)
    {
        boolean flag = false;

//        if (settings.useMonuments && mapFeaturesEnabled && chunkIn.getInhabitedTime() < 3600L)
//        {
//            flag |= oceanMonumentGenerator.generateStructure(world, rand, new ChunkPos(x, z));
//        }

        return flag;
    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
    {
        Biome biome = world.getBiome(pos);

//        if (mapFeaturesEnabled)
//        {
//            if (creatureType == EnumCreatureType.MONSTER && scatteredFeatureGenerator.isSwampHut(pos))
//            {
//                return scatteredFeatureGenerator.getMonsters();
//            }
//
//            if (creatureType == EnumCreatureType.MONSTER && settings.useMonuments && oceanMonumentGenerator.isPositionInStructure(world, pos))
//            {
//                return oceanMonumentGenerator.getMonsters();
//            }
//        }

        return biome.getSpawnableList(creatureType);
    }

    @Override
    public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos)
    {
        return false;
        
//        if (!mapFeaturesEnabled)
//        {
//            return false;
//        }
//        else if ("Stronghold".equals(structureName) && strongholdGenerator != null)
//        {
//            return strongholdGenerator.isInsideStructure(pos);
//        }
//        else if ("Monument".equals(structureName) && oceanMonumentGenerator != null)
//        {
//            return oceanMonumentGenerator.isInsideStructure(pos);
//        }
//        else if ("Village".equals(structureName) && villageGenerator != null)
//        {
//            return villageGenerator.isInsideStructure(pos);
//        }
//        else if ("Mineshaft".equals(structureName) && mineshaftGenerator != null)
//        {
//            return mineshaftGenerator.isInsideStructure(pos);
//        }
//        else
//        {
//            return "Temple".equals(structureName) && scatteredFeatureGenerator != null ? scatteredFeatureGenerator.isInsideStructure(pos) : false;
//        }
    }

    @Override
    @Nullable
    public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored)
    {
        return null;
        
//        if (!mapFeaturesEnabled)
//        {
//            return null;
//        }
//        else if ("Stronghold".equals(structureName) && strongholdGenerator != null)
//        {
//            return strongholdGenerator.getNearestStructurePos(worldIn, position, findUnexplored);
//        }
//        else if ("Monument".equals(structureName) && oceanMonumentGenerator != null)
//        {
//            return oceanMonumentGenerator.getNearestStructurePos(worldIn, position, findUnexplored);
//        }
//        else if ("Village".equals(structureName) && villageGenerator != null)
//        {
//            return villageGenerator.getNearestStructurePos(worldIn, position, findUnexplored);
//        }
//        else if ("Mineshaft".equals(structureName) && mineshaftGenerator != null)
//        {
//            return mineshaftGenerator.getNearestStructurePos(worldIn, position, findUnexplored);
//        }
//        else
//        {
//            return "Temple".equals(structureName) && scatteredFeatureGenerator != null
//                    ? scatteredFeatureGenerator.getNearestStructurePos(worldIn, position, findUnexplored)
//                    : null;
//        }
    }

    /**
     * Recreates data about structures intersecting given chunk (used for example by getPossibleCreatures), without placing any blocks. When called for the first time before any
     * chunk is generated - also initializes the internal state needed by getPossibleCreatures.
     */
    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z)
    {
//        if (mapFeaturesEnabled)
//        {
//            if (settings.useMineShafts)
//            {
//                mineshaftGenerator.generate(world, x, z, (ChunkPrimer) null);
//            }
//
//            if (settings.useVillages)
//            {
//                villageGenerator.generate(world, x, z, (ChunkPrimer) null);
//            }
//
//            if (settings.useStrongholds)
//            {
//                strongholdGenerator.generate(world, x, z, (ChunkPrimer) null);
//            }
//
//            if (settings.useTemples)
//            {
//                scatteredFeatureGenerator.generate(world, x, z, (ChunkPrimer) null);
//            }
//
//            if (settings.useMonuments)
//            {
//                oceanMonumentGenerator.generate(world, x, z, (ChunkPrimer) null);
//            }
//        }
    }
}