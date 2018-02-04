package com.blogspot.jabelarminecraft.examplemod.blocks;

import net.minecraft.block.BlockIce;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author jabelar
 *
 */
public class BlockCloud extends BlockIce
{

    public BlockCloud()
    {
        super();
        
        // DEBUG
        System.out.println("BlockCloud constructor");
        
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        blockSoundType = SoundType.SNOW;
        blockParticleGravity = 1.0F;
        slipperiness = 0.6F;
        lightOpacity = 20; 
        setTickRandomly(false);
        setLightLevel(0.5F); // redstone light has light value of 1.0F
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }
    
    @Override
    public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, net.minecraft.entity.EntityLiving.SpawnPlacementType type)
    {
        return true;
    }
    

    @Override
    @Deprecated
    @SideOnly(Side.CLIENT)
    public boolean hasCustomBreakingProgress(IBlockState state)
    {
        return false;
    }
}
