package com.blogspot.jabelarminecraft.examplemod.blocks;

import java.util.Random;

import com.blogspot.jabelarminecraft.examplemod.client.particles.ParticleEmoji;

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
        blockParticleGravity = 0.1F;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        double xPos = pos.getX() + 0.5D;
        double yPos = pos.getY() + 1.0D;
        double zPos = pos.getZ() + 0.5D;
        double xMotion = RANDOM.nextDouble()*1.0D;
        double yMotion = 2.0D;
        double zMotion = RANDOM.nextDouble()*1.0D;
        
        Particle theParticle = new ParticleEmoji(worldIn, xPos, yPos, zPos, xMotion, yMotion, zMotion);                
        Minecraft.getMinecraft().effectRenderer.addEffect(theParticle);
    }
}
