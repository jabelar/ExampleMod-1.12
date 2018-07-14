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
package com.blogspot.jabelarminecraft.examplemod.blocks;

import java.awt.Color;
import java.util.Random;

import com.blogspot.jabelarminecraft.examplemod.client.particles.ParticleCustom;
import com.blogspot.jabelarminecraft.examplemod.init.ModParticles;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// TODO: Auto-generated Javadoc
public class BlockParticleEmitter extends Block
{
    
    /**
     * Instantiates a new block particle emitter.
     */
    public BlockParticleEmitter()
    {
        super(Material.ROCK);

        // DEBUG
        System.out.println("BlockParticleEmitter constructor");

        blockSoundType = SoundType.STONE;
    }
    
    /* (non-Javadoc)
     * @see net.minecraft.block.Block#randomDisplayTick(net.minecraft.block.state.IBlockState, net.minecraft.world.World, net.minecraft.util.math.BlockPos, java.util.Random)
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        double xPos = pos.getX() + 0.5D;
        double yPos = pos.getY() + 1.0D;
        double zPos = pos.getZ() + 0.5D;
        double xMotion = 0.1D*RANDOM.nextDouble() - 0.05D;
        double yMotion = 0.3D;
        double zMotion = 0.1D*RANDOM.nextDouble() - 0.05D;
        
        Particle theParticle = new ParticleCustom(
                ModParticles.EMOJI_ANIMATED,
                worldIn, 
                xPos, yPos, zPos, 
                xMotion, yMotion, zMotion)
                .setRotSpeed(((float)Math.random() - 0.5F) * 0.1F)
                .setLifeSpan(20 + rand.nextInt(20))
                .setGravity(0.2F)
                .setScale(2.0F)
                .setInitialAlpha(1.0F)
                .setFinalAlpha(0.0F)
                .setInitalTint(Color.WHITE)
                .setFinalTint(Color.RED);
        Minecraft.getMinecraft().effectRenderer.addEffect(theParticle);
    }
}
