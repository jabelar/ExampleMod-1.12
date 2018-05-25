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

import java.awt.Color;

import com.blogspot.jabelarminecraft.examplemod.MainMod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
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
public class ParticleCustom extends Particle
{
    private static final double GRAVITY = 0.04D;
    private static final double GROUND_DECCEL = 0.7D;
    
    private TextureDefinition TEXTURE_DEF;
//    private String particleName;    
//    private static ResourceLocation PARTICLE_TEXTURE ;
    private static final VertexFormat VERTEX_FORMAT = (new VertexFormat()).addElement(DefaultVertexFormats.POSITION_3F).addElement(DefaultVertexFormats.TEX_2F).addElement(DefaultVertexFormats.COLOR_4UB).addElement(DefaultVertexFormats.TEX_2S).addElement(DefaultVertexFormats.NORMAL_3B).addElement(DefaultVertexFormats.PADDING_1B);

    private float rotSpeed = 0.0F;
    private float initialAlpha = 1.0F;
    private float finalAlpha = 1.0F;
    private Color initialTint = Color.WHITE;
    private Color finalTint = Color.WHITE;
    
    private float progress = 0.0F;
    
    private boolean enableDepth = true;
    
    private double deccel = 0.98D;

    public ParticleCustom(
            TextureDefinition parTexDef,
            World parWorld,
            double parX, double parY, double parZ)
    {
        super(parWorld, parX, parY, parZ);
        TEXTURE_DEF = parTexDef;
    }

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
    public ParticleCustom(
            TextureDefinition parTexDef,
            World parWorld,
            double parX, double parY, double parZ,
            double parMotionX, double parMotionY, double parMotionZ)
    {
        super(parWorld, parX, parY, parZ);
        TEXTURE_DEF = parTexDef;
        motionX = parMotionX;
        motionY = parMotionY;
        motionZ = parMotionZ;
    }
    
    @Override
    public void onUpdate()
    {
        updateTick();
        processGravityAndDeccel();
        move(motionX, motionY, motionZ);
        processAlphaTween();
        processTintTween();
    }
    
    private void updateTick()
    {
        if (particleAge++ >= particleMaxAge) { setExpired(); }
        progress = ((float)particleAge) / ((float)particleMaxAge);

        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        
        prevParticleAngle = particleAngle;
        particleAngle += (float)Math.PI * rotSpeed * 2.0F;
    }
    
    private void processGravityAndDeccel()
    {
        motionY -= GRAVITY * particleGravity;
        motionX *= deccel;
        motionY *= deccel;
        motionZ *= deccel;
        rotSpeed *= deccel;
        if (onGround && canCollide)
        {
            motionX *= GROUND_DECCEL * deccel;
            motionZ *= GROUND_DECCEL * deccel;
        }
    }
    
    protected void processAlphaTween()
    {
        particleAlpha = initialAlpha + progress * (finalAlpha - initialAlpha);
    }
    
    protected void processTintTween()
    {
        particleRed = (initialTint.getRed() + progress * (finalTint.getRed() - initialTint.getRed())) / 256.0F;
        particleBlue = (initialTint.getBlue() + progress * (finalTint.getBlue() - initialTint.getBlue())) / 256.0F;
        particleGreen = (initialTint.getGreen() + progress * (finalTint.getGreen() - initialTint.getGreen())) / 256.0F;
    }
    
    @Override
    public int getFXLayer()
    {
        return 3;
    }
    
    @Override
    public boolean shouldDisableDepth()
    {
        return (!enableDepth);
    }
    
    @Override
    public void renderParticle(
            BufferBuilder bufferIn, 
            Entity entityIn, 
            float partialTicks, 
            float rotationX, float rotationZ, 
            float rotationYZ, float rotationXY, float rotationXZ)
    {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc(516, 0.003921569F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, particleAlpha);

        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE_DEF.getResourceLocation());
   
        float uMin = TEXTURE_DEF.getUMin();
        float uMax = TEXTURE_DEF.getUMax();
        float vMin = TEXTURE_DEF.getVmin();
        float vMax = TEXTURE_DEF.getVMax();
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
        GlStateManager.popMatrix();
    } 
    
    /*
     * Chainable setter methods to help avoid needing multiple 
     * constructor overloads
     */
    
    
    public ParticleCustom setLifeSpan(int lifeIn)
    {
        particleMaxAge = lifeIn;
        return this;
    }
    
    public ParticleCustom setScale(float scaleIn)
    {
        particleScale = scaleIn;
        return this;
    }

    public ParticleCustom setTintColor(float redIn, float greenIn, float blueIn)
    {
        particleRed = redIn;
        particleGreen = greenIn;
        particleBlue = blueIn;
        return this;
    }
    
    public ParticleCustom setTintColor(Color colorIn)
    {
        particleGreen = colorIn.getGreen() / 256.0F;
        particleBlue = colorIn.getBlue() / 256.0F;
        particleRed = colorIn.getRed() / 256.0F;
        return this;
    }

    public ParticleCustom setTintColorAndAlpha(float redIn, float greenIn, float blueIn, float alphaIn)
    {
        particleRed = redIn;
        particleGreen = greenIn;
        particleBlue = blueIn;
        particleAlpha = alphaIn;
        return this;
    }

    public ParticleCustom setTintColorAndAlpha(Color colorIn)
    {
        particleGreen = colorIn.getGreen() / 256.0F;
        particleBlue = colorIn.getBlue() / 256.0F;
        particleRed = colorIn.getRed() / 256.0F;
        particleAlpha = colorIn.getAlpha() / 256.0F;
        return this;
    }
    
    public ParticleCustom setAlpha(float alphaIn)
    {
        particleAlpha = alphaIn;
        return this;
    }
    
    public ParticleCustom setAlpha(Color colorIn)
    {
        particleAlpha = colorIn.getAlpha() / 256.0F;
        return this;
    }
    
    public ParticleCustom setInitalTint(Color colorIn)
    {
        initialTint = colorIn;
        particleGreen = colorIn.getGreen() / 256.0F;
        particleBlue = colorIn.getBlue() / 256.0F;
        particleRed = colorIn.getRed() / 256.0F;
        return this;
    }
    
    public ParticleCustom setFinalTint(Color colorIn)
    {
        finalTint = colorIn;
        return this;
    }
    
    public ParticleCustom setInitialAlpha(float alphaIn)
    {
        initialAlpha = alphaIn;
        particleAlpha = alphaIn;
        return this;
    }
    
    public ParticleCustom setInitialAlpha(Color colorIn)
    {
        initialAlpha = colorIn.getAlpha() / 256.0F;
        particleAlpha = colorIn.getAlpha() / 256.0F;
        return this;
    }
    
    public ParticleCustom setFinalAlpha(float alphaIn)
    {
        finalAlpha = alphaIn;
        return this;
    }
    
    public ParticleCustom setFinalAlpha(Color colorIn)
    {
        finalAlpha = colorIn.getAlpha() / 256.0F;
        return this;
    }
   
    public ParticleCustom setGravity(float gravityIn)
    {
        particleGravity = gravityIn;
        return this;
    }
    
    public ParticleCustom setRotSpeed(float rotIn)
    {
        rotSpeed = rotIn;
        return this;
    }
    
    public ParticleCustom setEnableDepth(boolean enableDepthIn)
    {
        enableDepth = enableDepthIn;
        return this;
    }
    
    public static class TextureDefinition
    {
        private String name;
        private ResourceLocation resourceLocation;
        private float uMin;
        private float vMin;
        private float uMax;
        private float vMax;
        
        public TextureDefinition(String parName, float parUMin, float parVMin, float parUMax, float parVMax)
        {
            name = parName;
            resourceLocation = new ResourceLocation(MainMod.MODID, "textures/particles/"+name+".png");
            uMin = parUMin;
            vMin = parVMin;
            uMax = parUMax;
            vMax = parVMax;
        }
        
        /* 
         * Getter methods.
         */
        
        public String getName() { return name; };
        public ResourceLocation getResourceLocation() { return resourceLocation; };
        public float getUMin() { return uMin; };
        public float getVmin() { return vMin; };
        public float getUMax() { return uMax; };
        public float getVMax() { return vMax; };
    }
}
