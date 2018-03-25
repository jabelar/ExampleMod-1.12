package com.blogspot.jabelarminecraft.examplemod.init;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModBlockColors implements IBlockColor
{
    public static final IBlockColor INSTANCE = new ModBlockColors();
    public static Random rand = new Random();
    
    @Override
    public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex)
    {
//        return rand.nextInt(0xFFFFFF);
        return 0xFFFFFF;
    }
    
    public static void registerBlockColors()
    {
        // DEBUG
        System.out.println("Registering block color handler");
        
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(INSTANCE, ModBlocks.cloud_leaves);
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(INSTANCE, ModBlocks.cloud);
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(INSTANCE, ModBlocks.cloud_grass);
    }
}

