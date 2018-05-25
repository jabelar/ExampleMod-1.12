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

public class BlockParticleEmitter extends Block
{
    public BlockParticleEmitter()
    {
        super(Material.ROCK);

        // DEBUG
        System.out.println("BlockParticleEmitter constructor");

        blockSoundType = SoundType.STONE;
    }
    
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
                ModParticles.EMOJI,
                worldIn, 
                xPos, yPos, zPos, 
                xMotion, yMotion, zMotion)
                .setRotSpeed(((float)Math.random() - 0.5F) * 0.1F)
                .setLifeSpan(20 + rand.nextInt(20))
                .setGravity(0.2F)
                .setScale(2.0F)
                .setTintColor(Color.RED);                
        Minecraft.getMinecraft().effectRenderer.addEffect(theParticle);
    }
}
