package com.blogspot.jabelarminecraft.examplemod.worldgen;

import java.util.Random;

import com.blogspot.jabelarminecraft.examplemod.entities.EntityPigTest;
import com.blogspot.jabelarminecraft.examplemod.init.ModWorldGen;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeCloud extends Biome
{
    public BiomeCloud()
    {
        super(new BiomeProperties(ModWorldGen.CLOUD_NAME)
                .setBaseHeight(1.0F)
                .setHeightVariation(0.2F)
                .setRainDisabled()
                .setTemperature(0.2F)
                );
        
        topBlock = Blocks.GRASS.getDefaultState(); // ModBlocks.cloud.getDefaultState();
        fillerBlock = Blocks.STONE.getDefaultState(); // ModBlocks.cloud.getDefaultState();
        
        decorator = new BiomeDecoratorCloud();
        decorator.treesPerChunk = -999;
        decorator.deadBushPerChunk = 0;
        decorator.reedsPerChunk = 0;
        decorator.cactiPerChunk = 0;
        
        spawnableMonsterList.clear();
        spawnableCreatureList.clear();
        spawnableWaterCreatureList.clear();
        spawnableCaveCreatureList.clear();
        spawnableCreatureList.add(new Biome.SpawnListEntry(EntitySheep.class, 12, 4, 4));
        spawnableCreatureList.add(new Biome.SpawnListEntry(EntityPigTest.class, 10, 4, 4));
        spawnableCreatureList.add(new Biome.SpawnListEntry(EntityChicken.class, 10, 4, 4));
        spawnableCreatureList.add(new Biome.SpawnListEntry(EntityCow.class, 8, 4, 4));
        spawnableMonsterList.add(new Biome.SpawnListEntry(EntityGhast.class, 50, 4, 4));
        spawnableMonsterList.add(new Biome.SpawnListEntry(EntityPigZombie.class, 100, 4, 4));
        spawnableMonsterList.add(new Biome.SpawnListEntry(EntityMagmaCube.class, 2, 4, 4));
        spawnableMonsterList.add(new Biome.SpawnListEntry(EntityEnderman.class, 1, 4, 4));
        
        // DEBUG
        System.out.println("Constructing BiomeCloud");
    }
    
    @Override
    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal)
    {
//        // DEBUG
//        System.out.println("Generating terrain blocks");
        
        int seaLevel = worldIn.getSeaLevel();
//        IBlockState iblockstate = this.topBlock;
//        IBlockState iblockstate1 = this.fillerBlock;
        int indexInNoise = -1;
        int noise = (int)(noiseVal / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
        int xInChunk = x & 15;
        int zInChunk = z & 15;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (int y = 255; y >= 0; --y)
        {
            if (y <= rand.nextInt(5))
            {
                chunkPrimerIn.setBlockState(zInChunk, y, xInChunk, BEDROCK);
            }
            else
            {
                IBlockState blockStateAtY = chunkPrimerIn.getBlockState(zInChunk, y, xInChunk);

                if (blockStateAtY.getMaterial() == Material.AIR)
                {
                    indexInNoise = -1;
                }
                else if (blockStateAtY.getBlock() == Blocks.STONE)
//                else if (blockStateAtY.getBlock() == ModBlocks.cloud)
                {
                    if (indexInNoise == -1)
                    {
//                        if (noise <= 0)
//                        {
//                            topBlock = AIR;
//                            fillerBlock = STONE;
//                        }
//                        else if (j1 >= seaLevel - 4 && j1 <= seaLevel + 1)
//                        {
//                            topBlock = this.topBlock;
//                            fillerBlock = this.fillerBlock;
//                        }

                        if (y < seaLevel && (topBlock == null || topBlock.getMaterial() == Material.AIR))
                        {
                            if (this.getTemperature(blockpos$mutableblockpos.setPos(x, y, z)) < 0.15F)
                            {
                                topBlock = ICE;
                            }
                            else
                            {
                                topBlock = WATER;
                            }
                        }

                        indexInNoise = noise;

//                        if (j1 >= seaLevel - 1)
//                        {
//                            chunkPrimerIn.setBlockState(zInChunk, j1, xInChunk, topBlock);
//                        }
//                        else if (j1 < seaLevel - 7 - noise)
//                        {
//                            topBlock = AIR;
//                            fillerBlock = STONE;
//                            chunkPrimerIn.setBlockState(zInChunk, j1, xInChunk, GRAVEL);
//                        }
//                        else
                        {
                            chunkPrimerIn.setBlockState(zInChunk, y, xInChunk, fillerBlock);
                        }
                    }
                    else if (indexInNoise > 0)
                    {
                        --indexInNoise;
                        chunkPrimerIn.setBlockState(zInChunk, y, xInChunk, fillerBlock);
//
//                        if (j == 0 && fillerBlock.getBlock() == Blocks.SAND && noise > 1)
//                        {
//                            j = rand.nextInt(4) + Math.max(0, j1 - 63);
//                            fillerBlock = fillerBlock.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND ? RED_SANDSTONE : SANDSTONE;
//                        }
                    }
                }
            }
        }
    }
    
}
