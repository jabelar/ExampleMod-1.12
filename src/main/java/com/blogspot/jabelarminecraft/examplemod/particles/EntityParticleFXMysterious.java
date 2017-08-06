/**
    Copyright (C) 2014 by jabelar

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

package com.blogspot.jabelarminecraft.examplemod.particles;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;

/**
 * @author jabelar
 *
 */
public class EntityParticleFXMysterious extends Particle
{

	/**
	 * @param parWorld
	 * @param parX
	 * @param parY
	 * @param parZ
	 * @param parMotionX
	 * @param parMotionY
	 * @param parMotionZ
	 */
	public EntityParticleFXMysterious(World parWorld,
			double parX, double parY, double parZ,
			double parMotionX, double parMotionY, double parMotionZ) 
	{
		super(parWorld, parX, parY, parZ, parMotionX, parMotionY, parMotionZ);
		setParticleTextureIndex(82); // same as happy villager
		particleScale = 2.0F;
		setRBGColorF(0x88, 0x00, 0x88);
	}

}
