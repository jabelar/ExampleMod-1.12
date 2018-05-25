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

package com.blogspot.jabelarminecraft.examplemod.client.particles;

import org.lwjgl.opengl.GL11;

import com.blogspot.jabelarminecraft.examplemod.MainMod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author jabelar
 *
 */
@SideOnly(Side.CLIENT)
public class ParticleEmoji extends Particle
{
    private static final ResourceLocation PARTICLE_TEXTURE = new ResourceLocation(MainMod.MODID, "textures/particles/particle_emoji.png");
    private static final VertexFormat VERTEX_FORMAT = (new VertexFormat()).addElement(DefaultVertexFormats.POSITION_3F).addElement(DefaultVertexFormats.TEX_2F).addElement(DefaultVertexFormats.COLOR_4UB).addElement(DefaultVertexFormats.TEX_2S).addElement(DefaultVertexFormats.NORMAL_3B).addElement(DefaultVertexFormats.PADDING_1B);

    /**
     * Instantiates a new entity particle FX mysterious.
     *
     * @param parWorld the par world
     * @param parX the par X
     * @param parY the par Y
     * @param parZ the par Z
     * @param parMotionX the par motion X
     * @param parMotionY the par motion Y
     * @param parMotionZ the par motion Z
     */
    public ParticleEmoji(
            World parWorld,
            double parX, double parY, double parZ,
            double parMotionX, double parMotionY, double parMotionZ)
    {
        super(parWorld, parX, parY, parZ, parMotionX, parMotionY, parMotionZ);
        particleScale = 2.0F;
        particleGravity = 0.2F;
    }
    
    @Override
    public void onUpdate()
    {
        super.onUpdate();
    }
    
    @Override
    public int getFXLayer()
    {
        return 3;
    }
    
    @Override
    public void renderParticle(
            BufferBuilder bufferIn, 
            Entity entityIn, 
            float partialTicks, 
            float rotationX, float rotationZ, 
            float rotationYZ, float rotationXY, float rotationXZ)
    {
        GL11.glPushMatrix();
        GL11.glDepthFunc(GL11.GL_ALWAYS);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, particleAlpha);
        GlStateManager.disableLighting();
        RenderHelper.disableStandardItemLighting();

        Minecraft.getMinecraft().getTextureManager().bindTexture(PARTICLE_TEXTURE);
   
        float uMin = 0.0F;
        float uMax = 1.0f;
        float vMin = 0.0F;
        float vMax = 1.0f;
        float scale = 0.1F * particleScale;
        float xInterp = (float)(prevPosX + (posX - prevPosX) * partialTicks - interpPosX);
        float yInterp = (float)(prevPosY + (posY - prevPosY) * partialTicks - interpPosY);
        float zInterp = (float)(prevPosZ + (posZ - prevPosZ) * partialTicks - interpPosZ);

        bufferIn.begin(7, VERTEX_FORMAT);
        bufferIn.pos(
                xInterp - rotationX * scale - rotationXY * scale,
                yInterp - rotationZ * scale,
                zInterp - rotationYZ * scale - rotationXZ * scale)
                .tex(uMax, vMax)
                .color(particleRed, particleGreen, particleBlue, 1.0F)
                .lightmap(0, 240)
                .normal(0.0F, 1.0F, 0.0F)
                .endVertex();
        bufferIn.pos(
                xInterp - rotationX * scale + rotationXY * scale, 
                yInterp + rotationZ * scale, 
                zInterp - rotationYZ * scale + rotationXZ * scale)
                .tex(uMax, vMin)
                .color(particleRed, particleGreen, particleBlue, 1.0F)
                .lightmap(0, 240).normal(0.0F, 1.0F, 0.0F)
                .endVertex();
        bufferIn.pos(
                xInterp + rotationX * scale + rotationXY * scale, 
                yInterp + rotationZ * scale, 
                zInterp + rotationYZ * scale + rotationXZ * scale)
                .tex(uMin, vMin)
                .color(particleRed, particleGreen, particleBlue, 1.0F)
                .lightmap(0, 240)
                .normal(0.0F, 1.0F, 0.0F)
                .endVertex();
        bufferIn.pos(
                xInterp + rotationX * scale - rotationXY * scale, 
                yInterp - rotationZ * scale, 
                zInterp + rotationYZ * scale - rotationXZ * scale)
                .tex(uMin, vMax)
                .color(particleRed, particleGreen, particleBlue, 1.0F)
                .lightmap(0, 240)
                .normal(0.0F, 1.0F, 0.0F)
                .endVertex();
        Tessellator.getInstance().draw();
        GlStateManager.enableLighting();
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glPopMatrix();
    } 
}
