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

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

// TODO: Auto-generated Javadoc
public class ModFluid extends Fluid
{
	protected static int mapColor = 0xFFFFFFFF;
	protected static float overlayAlpha = 0.2F;
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
	
	public ModFluid(String fluidName, ResourceLocation still, ResourceLocation flowing, int mapColor) 
	{
		this(fluidName, still, flowing);
		setColor(mapColor);
	}
	
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
	public Fluid setColor(int parColor)
	{
		mapColor = parColor;
		return this;
	}
	
	public float getAlpha()
	{
		return overlayAlpha;
	}
	
	public Fluid setAlpha(float parOverlayAlpha)
	{
		overlayAlpha = parOverlayAlpha;
		return this;
	}
}
