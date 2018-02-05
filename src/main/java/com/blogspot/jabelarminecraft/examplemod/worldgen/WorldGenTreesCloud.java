package com.blogspot.jabelarminecraft.examplemod.worldgen;

import java.util.Random;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.IPlantable;

public class WorldGenTreesCloud extends WorldGenAbstractTree
{
    private IBlockState blockStateWood = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK);
    private IBlockState blockStateLeaves = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
    /** The minimum height of a generated tree. */
    private final int minTreeHeight = 4;

    public WorldGenTreesCloud(boolean parShouldNotify)
    {
        super(parShouldNotify);
    }

    @Override
    public boolean generate(World parWorld, Random parRandom, BlockPos parBlockPos)
    {
        int minHeight = parRandom.nextInt(3) + minTreeHeight;
        boolean isSuitableLocation = true;
        
        // Check if tree fits in world
        if (parBlockPos.getY() >= 1 && parBlockPos.getY() + minHeight + 1 <= parWorld.getHeight())
        {
            for (int checkY = parBlockPos.getY(); checkY <= parBlockPos.getY() + 1 + minHeight; ++checkY)
            {
                // Handle increasing space towards top of tree
                int extraSpaceNeeded = 1;
                // Handle base location
                if (checkY == parBlockPos.getY())
                {
                    extraSpaceNeeded = 0;
                }             
                // Handle top location
                if (checkY >= parBlockPos.getY() + 1 + minHeight - 2)
                {
                    extraSpaceNeeded = 2;
                }

                BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();

                for (int checkX = parBlockPos.getX() - extraSpaceNeeded; checkX <= parBlockPos.getX() + extraSpaceNeeded && isSuitableLocation; ++checkX)
                {
                    for (int checkZ = parBlockPos.getZ() - extraSpaceNeeded; checkZ <= parBlockPos.getZ() + extraSpaceNeeded && isSuitableLocation; ++checkZ)
                    {
                        isSuitableLocation = isReplaceable(parWorld,blockPos.setPos(checkX, checkY, checkZ));
                    }
                }
            }

            if (!isSuitableLocation)
            {
                return false;
            }
            else
            {
                IBlockState state = parWorld.getBlockState(parBlockPos.down());

                if (state.getBlock().canSustainPlant(state, parWorld, parBlockPos.down(), EnumFacing.UP, (IPlantable) Blocks.SAPLING) && parBlockPos.getY() < parWorld.getHeight() - minHeight - 1)
                {
                    state.getBlock().onPlantGrow(state, parWorld, parBlockPos.down(), parBlockPos);

                    for (int i3 = parBlockPos.getY() - 3 + minHeight; i3 <= parBlockPos.getY() + minHeight; ++i3)
                    {
                        int i4 = i3 - (parBlockPos.getY() + minHeight);
                        int j1 = 1 - i4 / 2;

                        for (int k1 = parBlockPos.getX() - j1; k1 <= parBlockPos.getX() + j1; ++k1)
                        {
                            int l1 = k1 - parBlockPos.getX();

                            for (int i2 = parBlockPos.getZ() - j1; i2 <= parBlockPos.getZ() + j1; ++i2)
                            {
                                int j2 = i2 - parBlockPos.getZ();

                                if (Math.abs(l1) != j1 || Math.abs(j2) != j1 || parRandom.nextInt(2) != 0 && i4 != 0)
                                {
                                    BlockPos blockpos = new BlockPos(k1, i3, i2);
                                    state = parWorld.getBlockState(blockpos);

                                    if (state.getBlock().isAir(state, parWorld, blockpos) || state.getBlock().isLeaves(state, parWorld, blockpos) || state.getMaterial() == Material.VINE)
                                    {
                                        setBlockAndNotifyAdequately(parWorld, blockpos, blockStateLeaves);
                                    }
                                }
                            }
                        }
                    }

                    for (int j3 = 0; j3 < minHeight; ++j3)
                    {
                        BlockPos upN = parBlockPos.up(j3);
                        state = parWorld.getBlockState(upN);

                        if (state.getBlock().isAir(state, parWorld, upN) || state.getBlock().isLeaves(state, parWorld, upN) || state.getMaterial() == Material.VINE)
                        {
                            setBlockAndNotifyAdequately(parWorld, parBlockPos.up(j3), blockStateWood);
                        }
                    }

                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        else
        {
            return false;
        }
    }
}