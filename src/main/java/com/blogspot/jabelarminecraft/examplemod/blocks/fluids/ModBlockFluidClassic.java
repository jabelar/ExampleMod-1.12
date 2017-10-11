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
package com.blogspot.jabelarminecraft.examplemod.blocks.fluids;

import java.awt.Color;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// TODO: Auto-generated Javadoc
public class ModBlockFluidClassic extends BlockFluidClassic
{
    /**
     * Instantiates a new mod block fluid classic.
     *
     * @param parFluid
     *            the par fluid
     * @param parMaterial
     *            the par material
     */
    public ModBlockFluidClassic(Fluid parFluid, Material parMaterial)
    {
        super(parFluid, parMaterial);
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.minecraftforge.fluids.BlockFluidBase#modifyAcceleration(net.minecraft.world.World, net.minecraft.util.math.BlockPos, net.minecraft.entity.Entity,
     * net.minecraft.util.math.Vec3d)
     */
    @Override
    public Vec3d modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3d motion)
    {
        // // DEBUG
        // System.out.println("modifyAcceleration for "+entityIn+" with isPushedByWater() = "+entityIn.isPushedByWater());

        if (worldIn.getBlockState(pos).getMaterial() instanceof MaterialLiquid)
        {
            Vec3d flowAdder = getFlow(worldIn, pos, worldIn.getBlockState(pos));

            // // DEBUG
            // System.out.println("may push entity with motion adder = "+flowAdder);

            return motion.add(flowAdder);
        }
        else
        {
            // // DEBUG
            // System.out.println("may not push entity");

            return motion;
        }
    }

    /**
     * Gets the flow.
     *
     * @param worldIn
     *            the world in
     * @param pos
     *            the pos
     * @param state
     *            the state
     * @return the flow
     */
    protected Vec3d getFlow(IBlockAccess worldIn, BlockPos pos, IBlockState state)
    {
        double d0 = 0.0D;
        double d1 = 0.0D;
        double d2 = 0.0D;
        int i = this.getRenderedDepth(state);
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
        {
            blockpos$pooledmutableblockpos.setPos(pos).move(enumfacing);
            int j = this.getRenderedDepth(worldIn.getBlockState(blockpos$pooledmutableblockpos));

            if (j < 0)
            {
                if (!worldIn.getBlockState(blockpos$pooledmutableblockpos).getMaterial().blocksMovement())
                {
                    j = this.getRenderedDepth(worldIn.getBlockState(blockpos$pooledmutableblockpos.down()));

                    if (j >= 0)
                    {
                        int k = j - (i - 8);
                        d0 += enumfacing.getFrontOffsetX() * k;
                        d1 += enumfacing.getFrontOffsetY() * k;
                        d2 += enumfacing.getFrontOffsetZ() * k;
                    }
                }
            }
            else if (j >= 0)
            {
                int l = j - i;
                d0 += enumfacing.getFrontOffsetX() * l;
                d1 += enumfacing.getFrontOffsetY() * l;
                d2 += enumfacing.getFrontOffsetZ() * l;
            }
        }

        Vec3d vec3d = new Vec3d(d0, d1, d2);

        if (state.getValue(LEVEL).intValue() >= 8)
        {
            // // DEBUG
            // System.out.println("fluid level greater than zero");

            for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL)
            {
                blockpos$pooledmutableblockpos.setPos(pos).move(enumfacing1);

                if (this.causesDownwardCurrent(worldIn, blockpos$pooledmutableblockpos, enumfacing1)
                        || this.causesDownwardCurrent(worldIn, blockpos$pooledmutableblockpos.up(), enumfacing1))
                {
                    // // DEBUG
                    // System.out.println("Causes downward current");

                    vec3d = vec3d.normalize().addVector(0.0D, -6.0D, 0.0D);
                    break;
                }
            }
        }

        blockpos$pooledmutableblockpos.release();
        return vec3d.normalize();
    }

    /**
     * Gets the depth.
     *
     * @param state
     *            the state
     * @return the depth
     */
    protected int getDepth(IBlockState state)
    {
        return state.getMaterial() == this.blockMaterial ? state.getValue(LEVEL).intValue() : -1;
    }

    /**
     * Gets the rendered depth.
     *
     * @param state
     *            the state
     * @return the rendered depth
     */
    protected int getRenderedDepth(IBlockState state)
    {
        int i = this.getDepth(state);
        return i >= 8 ? 0 : i;
    }

    /**
     * Checks if an additional {@code -6} vertical drag should be applied to the entity. See {#link net.minecraft.block.BlockLiquid#getFlow()}
     */
    private boolean causesDownwardCurrent(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();
        Material material = iblockstate.getMaterial();

        if (material == this.blockMaterial)
        {
            return false;
        }
        else if (side == EnumFacing.UP)
        {
            return true;
        }
        else if (material == Material.ICE)
        {
            return false;
        }
        else
        {
            boolean flag = isExceptBlockForAttachWithPiston(block) || block instanceof BlockStairs;
            return !flag && iblockstate.getBlockFaceShape(worldIn, pos, side) == BlockFaceShape.SOLID;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.minecraftforge.fluids.BlockFluidClassic#place(net.minecraft.world.World, net.minecraft.util.math.BlockPos, net.minecraftforge.fluids.FluidStack, boolean)
     */
    /* IFluidBlock */
    @Override
    public int place(World world, BlockPos pos, @Nonnull FluidStack fluidStack, boolean doPlace)
    {

        if (doPlace)
        {
            FluidUtil.destroyBlockOnFluidPlacement(world, pos);
            world.setBlockState(pos, this.getDefaultState(), 11);
        }
        return fluidStack.amount;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Vec3d getFogColor(World world, BlockPos pos, IBlockState state, Entity entity, Vec3d originalColor, float partialTicks)
    {
        return new Vec3d(Color.GREEN.getRed(), Color.GREEN.getGreen(), Color.GREEN.getBlue());
    }

}
