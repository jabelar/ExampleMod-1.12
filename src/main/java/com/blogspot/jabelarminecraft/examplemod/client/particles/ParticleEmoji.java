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
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
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
    private static final ResourceLocation PARTICLE_TEXTURE = new ResourceLocation(MainMod.MODID, "textures/particles/particle_emoji_hi_res.png");
    private static final VertexFormat VERTEX_FORMAT = (new VertexFormat()).addElement(DefaultVertexFormats.POSITION_3F).addElement(DefaultVertexFormats.TEX_2F).addElement(DefaultVertexFormats.COLOR_4UB).addElement(DefaultVertexFormats.TEX_2S).addElement(DefaultVertexFormats.NORMAL_3B).addElement(DefaultVertexFormats.PADDING_1B);

    final float rotSpeed;

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
        rotSpeed = ((float)Math.random() - 0.5F) * 0.1F;
    }
    
    @Override
    public void onUpdate()
    {
        super.onUpdate();
        prevParticleAngle = particleAngle;
        particleAngle += (float)Math.PI * rotSpeed * 2.0F;
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

        Minecraft.getMinecraft().getTextureManager().bindTexture(PARTICLE_TEXTURE);
   
        float uMin = 0.0F;
        float uMax = 1.0f;
        float vMin = 0.0F;
        float vMax = 1.0f;
        float scale = 0.1F * particleScale;
        float xInterp = (float)(prevPosX + (posX - prevPosX) * partialTicks - interpPosX);
        float yInterp = (float)(prevPosY + (posY - prevPosY) * partialTicks - interpPosY);
        float zInterp = (float)(prevPosZ + (posZ - prevPosZ) * partialTicks - interpPosZ);

        Vec3d[] avec3d = new Vec3d[] {new Vec3d(-rotationX * scale - rotationXY * scale, -rotationZ * scale, -rotationYZ * scale - rotationXZ * scale), new Vec3d(-rotationX * scale + rotationXY * scale, rotationZ * scale, -rotationYZ * scale + rotationXZ * scale), new Vec3d(rotationX * scale + rotationXY * scale, rotationZ * scale, rotationYZ * scale + rotationXZ * scale), new Vec3d(rotationX * scale - rotationXY * scale, -rotationZ * scale, rotationYZ * scale - rotationXZ * scale)};

        if (particleAngle != 0.0F)
        {
            float angleInterp = particleAngle + (particleAngle - prevParticleAngle) * partialTicks;
            float f9 = MathHelper.cos(angleInterp * 0.5F);
            float xComponent = MathHelper.sin(angleInterp * 0.5F) * (float)cameraViewDir.x;
            float yComponent = MathHelper.sin(angleInterp * 0.5F) * (float)cameraViewDir.y;
            float zComponent = MathHelper.sin(angleInterp * 0.5F) * (float)cameraViewDir.z;
            Vec3d vec3d = new Vec3d(xComponent, yComponent, zComponent);

            for (int l = 0; l < 4; ++l)
            {
                avec3d[l] = vec3d.scale(2.0D * avec3d[l].dotProduct(vec3d)).add(avec3d[l].scale(f9 * f9 - vec3d.dotProduct(vec3d))).add(vec3d.crossProduct(avec3d[l]).scale(2.0F * f9));
            }
        }
        
        int brightness = getBrightnessForRender(partialTicks);
        int j = brightness >> 16 & 65535;
        int k = brightness & 65535;

        bufferIn.begin(7, VERTEX_FORMAT);
        bufferIn.pos(
                xInterp + avec3d[0].x,
                yInterp + avec3d[0].y,
                zInterp + avec3d[0].z)
                .tex(uMax, vMax)
                .color(particleRed, particleGreen, particleBlue, particleAlpha)
                .lightmap(j, k)
                .normal(0.0F, 1.0F, 0.0F)
                .endVertex();
        bufferIn.pos(
                xInterp + avec3d[1].x,
                yInterp + avec3d[1].y,
                zInterp + avec3d[1].z)
                .tex(uMax, vMin)
                .color(particleRed, particleGreen, particleBlue, particleAlpha)
                .lightmap(j, k)
                .normal(0.0F, 1.0F, 0.0F)
                .endVertex();
        bufferIn.pos(
                xInterp + avec3d[2].x,
                yInterp + avec3d[2].y,
                zInterp + avec3d[2].z)
                .tex(uMin, vMin)
                .color(particleRed, particleGreen, particleBlue, particleAlpha)
                .lightmap(j, k)
                .normal(0.0F, 1.0F, 0.0F)
                .endVertex();
        bufferIn.pos(
                xInterp + avec3d[3].x,
                yInterp + avec3d[3].y,
                zInterp + avec3d[3].z)
                .tex(uMin, vMax)
                .color(particleRed, particleGreen, particleBlue, particleAlpha)
                .lightmap(j, k)
                .normal(0.0F, 1.0F, 0.0F)
                .endVertex();

        Tessellator.getInstance().draw();
        GL11.glPopMatrix();
    } 
}
