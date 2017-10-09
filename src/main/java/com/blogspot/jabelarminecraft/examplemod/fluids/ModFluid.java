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
package com.blogspot.jabelarminecraft.examplemod.fluids;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fluids.Fluid;

// TODO: Auto-generated Javadoc
public class ModFluid extends Fluid
{
	protected int mapColor = 0xFFFFFFFF;
	protected float overlayAlpha = 0.2F;
	protected SoundEvent emptySound = SoundEvents.ITEM_BUCKET_EMPTY;
	protected SoundEvent fillSound = SoundEvents.ITEM_BUCKET_FILL;
	
	/**
	 * Instantiates a new mod fluid.
	 *
	 * @param fluidName the fluid name
	 * @param still the still
	 * @param flowing the flowing
	 */
	public ModFluid(String fluidName, ResourceLocation still, ResourceLocation flowing) 
	{
		super(fluidName, still, flowing);
	}
	
	/**
	 * Instantiates a new mod fluid.
	 *
	 * @param fluidName the fluid name
	 * @param still the still
	 * @param flowing the flowing
	 * @param mapColor the map color
	 */
	public ModFluid(String fluidName, ResourceLocation still, ResourceLocation flowing, int mapColor) 
	{
		this(fluidName, still, flowing);
		setColor(mapColor);
	}
	
	/**
	 * Instantiates a new mod fluid.
	 *
	 * @param fluidName the fluid name
	 * @param still the still
	 * @param flowing the flowing
	 * @param mapColor the map color
	 * @param overlayAlpha the overlay alpha
	 */
	public ModFluid(String fluidName, ResourceLocation still, ResourceLocation flowing, int mapColor, float overlayAlpha) 
	{
		this(fluidName, still, flowing, mapColor);
		setAlpha(overlayAlpha);
	}

	
	/* (non-Javadoc)
	 * @see net.minecraftforge.fluids.Fluid#getColor()
	 */
	@Override
	public int getColor()
	{
		return mapColor;
	}
	
	/**
	 * Sets the color.
	 *
	 * @param parColor the par color
	 * @return the fluid
	 */
	public ModFluid setColor(int parColor)
	{
		mapColor = parColor;
		return this;
	}
	
	/**
	 * Gets the alpha.
	 *
	 * @return the alpha
	 */
	public float getAlpha()
	{
		return overlayAlpha;
	}
	
	/**
	 * Sets the alpha.
	 *
	 * @param parOverlayAlpha the par overlay alpha
	 * @return the fluid
	 */
	public ModFluid setAlpha(float parOverlayAlpha)
	{
		overlayAlpha = parOverlayAlpha;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see net.minecraftforge.fluids.Fluid#setEmptySound(net.minecraft.util.SoundEvent)
	 */
	@Override
	public ModFluid setEmptySound(SoundEvent parSound)
	{
		emptySound = parSound;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see net.minecraftforge.fluids.Fluid#getEmptySound()
	 */
	@Override
	public SoundEvent getEmptySound()
	{
		return emptySound;
	}
	
	/* (non-Javadoc)
	 * @see net.minecraftforge.fluids.Fluid#setFillSound(net.minecraft.util.SoundEvent)
	 */
	@Override
	public ModFluid setFillSound(SoundEvent parSound)
	{
		fillSound = parSound;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see net.minecraftforge.fluids.Fluid#getFillSound()
	 */
	@Override
	public SoundEvent getFillSound()
	{
		return fillSound;
	}	
}
