package com.blogspot.jabelarminecraft.examplemod.blocks.fluids;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlockFluidClassic extends BlockFluidClassic
{
	protected static boolean pushesEntity = false;

	public ModBlockFluidClassic(Fluid parFluid, Material parMaterial) 
	{
		super(parFluid, parMaterial);
	}

	public ModBlockFluidClassic(Fluid parFluid, Material parMaterial, boolean parPushesEntity) 
	{
		this(parFluid, parMaterial);
		setPushesEntity(parPushesEntity);
	}
	
	public static boolean getPushesEntity()
	{
		return pushesEntity;
	}

	public static void setPushesEntity(boolean parPushesEntity)
	{
		pushesEntity = parPushesEntity;
	}

    @Override
	public Vec3d modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3d motion)
    {
//    	// DEBUG
//    	System.out.println("modifyAcceleration for "+entityIn+" with isPushedByWater() = "+entityIn.isPushedByWater());
    	
    	if (getPushesEntity())
    	{
    		Vec3d flowAdder = getFlow(worldIn, pos, worldIn.getBlockState(pos));

//    		// DEBUG
//    		System.out.println("may push entity with motion adder = "+flowAdder);
    		
			return motion.add(flowAdder);
    	}
    	else
    	{
//    		// DEBUG
//    		System.out.println("may not push entity");
    		
    		return motion;
    	}
    }

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
//        	// DEBUG
//        	System.out.println("fluid level greater than zero");
        	
            for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL)
            {
                blockpos$pooledmutableblockpos.setPos(pos).move(enumfacing1);

                if (this.causesDownwardCurrent(worldIn, blockpos$pooledmutableblockpos, enumfacing1) || this.causesDownwardCurrent(worldIn, blockpos$pooledmutableblockpos.up(), enumfacing1))
                {
//                	// DEBUG
//                	System.out.println("Causes downward current");
                	
                    vec3d = vec3d.normalize().addVector(0.0D, -6.0D, 0.0D);
                    break;
                }
            }
        }

        blockpos$pooledmutableblockpos.release();
        return vec3d.normalize();
    }

    protected int getDepth(IBlockState state)
    {
        return state.getMaterial() == this.blockMaterial ? state.getValue(LEVEL).intValue() : -1;
    }

    protected int getRenderedDepth(IBlockState state)
    {
        int i = this.getDepth(state);
        return i >= 8 ? 0 : i;
    }

    /**
     * Checks if an additional {@code -6} vertical drag should be applied to the entity. See {#link
     * net.minecraft.block.BlockLiquid#getFlow()}
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
    
    /**
     * Use this to change the fog color used when the entity is "inside" a material.
     * Vec3d is used here as "r/g/b" 0 - 1 values.
     *
     * @param world         The world.
     * @param pos           The position at the entity viewport.
     * @param state         The state at the entity viewport.
     * @param entity        the entity
     * @param originalColor The current fog color, You are not expected to use this, Return as the default if applicable.
     * @return The new fog color.
     */
    @Override
	@SideOnly (Side.CLIENT)
    public Vec3d getFogColor(World world, BlockPos pos, IBlockState state, Entity entity, Vec3d originalColor, float partialTicks)
    {
        Vec3d viewport = net.minecraft.client.renderer.ActiveRenderInfo.projectViewFromEntity(entity, partialTicks);

        if (state.getMaterial().isLiquid())
        {
            float height = 0.0F;
            if (state.getBlock() instanceof ModBlockFluidClassic)
            {
            	height = (state.getValue(LEVEL) + 1) / 9.0F - 0.111111F; 
            }
            float f1 = pos.getY() + 1 - height;
            if (viewport.y > f1)
            {
                BlockPos upPos = pos.up();
                IBlockState upState = world.getBlockState(upPos);
                return upState.getBlock().getFogColor(world, upPos, upState, entity, originalColor, partialTicks);
            }
        }

        if (state.getMaterial() == Material.WATER)
        {
            float f12 = 0.0F;

            if (entity instanceof net.minecraft.entity.EntityLivingBase)
            {
                net.minecraft.entity.EntityLivingBase ent = (net.minecraft.entity.EntityLivingBase)entity;
                f12 = net.minecraft.enchantment.EnchantmentHelper.getRespirationModifier(ent) * 0.2F;

                if (ent.isPotionActive(net.minecraft.init.MobEffects.WATER_BREATHING))
                {
                    f12 = f12 * 0.3F + 0.6F;
                }
            }
            return new Vec3d(0.02F + f12, 0.02F + f12, 0.2F + f12);
        }
        else if (state.getMaterial() == Material.LAVA)
        {
            return new Vec3d(0.6F, 0.1F, 0.0F);
        }

    	Color theColor = new Color(getFluid().getColor());
        return new Vec3d(theColor.getRed(), theColor.getGreen(), theColor.getBlue());
    }
}
