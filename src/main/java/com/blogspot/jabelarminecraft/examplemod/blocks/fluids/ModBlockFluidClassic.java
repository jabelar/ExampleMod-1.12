package com.blogspot.jabelarminecraft.examplemod.blocks.fluids;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class ModBlockFluidClassic extends BlockFluidClassic
{

	public ModBlockFluidClassic(Fluid fluid, Material material) 
	{
		super(fluid, material);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState parIBlockState)
	{
		return EnumBlockRenderType.LIQUID;
	}
}
