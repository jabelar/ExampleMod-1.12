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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// TODO: Auto-generated Javadoc
/**
 * This is an example for my tutorial on custom particles. 
 * See: <a href="http://jabelarminecraft.blogspot.com/p/minecraft-modding-spawning-custom.html">Jabelar's Custom Particle Tutorial</a>
 * 
 * @author jabelar
 *
 */
@SideOnly(Side.CLIENT)
public class ParticleCustom extends Particle
{
    /*
     * Constants
     */
    private static final double GRAVITY = 0.04D;
    private static final double GROUND_DECCEL = 0.7D;
    private static final VertexFormat VERTEX_FORMAT = (new VertexFormat()).addElement(DefaultVertexFormats.POSITION_3F).addElement(DefaultVertexFormats.TEX_2F).addElement(DefaultVertexFormats.COLOR_4UB).addElement(DefaultVertexFormats.TEX_2S).addElement(DefaultVertexFormats.NORMAL_3B).addElement(DefaultVertexFormats.PADDING_1B);
    
    private TextureDefinition TEXTURE_DEF;

    /*
     * Animation-related fields
     */
    private int currentAnimationFrame = 0;
    private float progress = 0.0F;
    private float rotSpeed = 0.0F;
    private double deccel = 0.98D;
    private float initialAlpha = 1.0F;
    private float finalAlpha = 1.0F;
    private Color initialTint = Color.WHITE;
    private Color finalTint = Color.WHITE;
    private float initialScale = 1.0F;
    private float finalScale = 1.0F;
    
    private boolean enableDepth = true;
    
    

    /**
     * Instantiates a new custom particle.
     *
     * @param parTexDef the tex def
     * @param parWorld the world
     * @param parX the X
     * @param parY the Y
     * @param parZ the Z
     */
    public ParticleCustom(
            TextureDefinition parTexDef,
            World parWorld,
            double parX, double parY, double parZ)
    {
        super(parWorld, parX, parY, parZ);
        TEXTURE_DEF = parTexDef;
    }

    /**
     * Instantiates a new custom particle.
     *
     * @param parTexDef the tex def
     * @param parWorld the world
     * @param parX the X
     * @param parY the Y
     * @param parZ the Z
     * @param parMotionX the motion X
     * @param parMotionY the motion Y
     * @param parMotionZ the motion Z
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
    
    /* (non-Javadoc)
     * @see net.minecraft.client.particle.Particle#onUpdate()
     */
    @Override
    public void onUpdate()
    {
        updateTick();
        processGravityAndDeccel();
        move(motionX, motionY, motionZ);
        processAlphaTween();
        processTintTween();
        processScaleTween();
    }
    
    private void updateTick()
    {
        if (particleAge++ >= particleMaxAge) { setExpired(); }
        progress = ((float)particleAge) / ((float)particleMaxAge);
  
        if (TEXTURE_DEF.isTweenAnimationMode())
        {
            currentAnimationFrame = (int) (progress * (TEXTURE_DEF.getAnimationFrameCount() + 1)) ;
        }
        else
        {
            if (currentAnimationFrame++ >= TEXTURE_DEF.getAnimationFrameCount()) { currentAnimationFrame = 0; }
        }
        
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
    
    /**
     * Process alpha tween.
     */
    protected void processAlphaTween()
    {
        particleAlpha = initialAlpha + progress * (finalAlpha - initialAlpha);
    }
    
    /**
     * Process tint tween.
     */
    protected void processTintTween()
    {
        particleRed = (initialTint.getRed() + progress * (finalTint.getRed() - initialTint.getRed())) / 256.0F;
        particleBlue = (initialTint.getBlue() + progress * (finalTint.getBlue() - initialTint.getBlue())) / 256.0F;
        particleGreen = (initialTint.getGreen() + progress * (finalTint.getGreen() - initialTint.getGreen())) / 256.0F;
    }
    
    /**
     * Process scale tween.
     */
    protected void processScaleTween()
    {
        particleScale = initialScale + progress * (finalScale - initialScale);
    }
    
    /* (non-Javadoc)
     * @see net.minecraft.client.particle.Particle#getFXLayer()
     */
    @Override
    public int getFXLayer()
    {
        return 3;
    }
    
    /* (non-Javadoc)
     * @see net.minecraft.client.particle.Particle#shouldDisableDepth()
     */
    @Override
    public boolean shouldDisableDepth()
    {
        return (!enableDepth);
    }
    
    /* (non-Javadoc)
     * @see net.minecraft.client.particle.Particle#renderParticle(net.minecraft.client.renderer.BufferBuilder, net.minecraft.entity.Entity, float, float, float, float, float, float)
     */
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
        float frameV = currentAnimationFrame * (TEXTURE_DEF.getVMax() - TEXTURE_DEF.getVmin());
        float vMin = TEXTURE_DEF.getVmin() + frameV;
        float vMax = TEXTURE_DEF.getVMax() + frameV;
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
      
    /**
     * Sets the life span.
     *
     * @param lifeIn the life in
     * @return the custom particle
     */
    public ParticleCustom setLifeSpan(int lifeIn)
    {
        particleMaxAge = lifeIn;
        return this;
    }
    
    /**
     * Sets the can collide.
     *
     * @param canCollideIn the can collide in
     * @return the custom particle
     */
    public ParticleCustom setCanCollide(boolean canCollideIn)
    {
        canCollide = canCollideIn;
        return this;
    }
    
    /**
     * Sets the collision bounding box for the particle.
     *
     * @param boundingBoxIn the bounding box in
     * @return the custom particle
     */
    public ParticleCustom setAABB(AxisAlignedBB boundingBoxIn)
    {
        setBoundingBox(boundingBoxIn);
        return this;
    }
    
    /**
     * Sets the decceleration (like an "air friction" effect). 
     * This affects all motion axes.
     *
     * @param deccelIn the decceleration in
     * @return the custom particle
     */
    public ParticleCustom setDeccel(double deccelIn)
    {
        deccel = deccelIn;
        return this;
    }
    
    /**
     * Sets the particle scale, including removing any scale animation.
     *
     * @param scaleIn the scale in
     * @return the custom particle
     */
    public ParticleCustom setScale(float scaleIn)
    {
        particleScale = scaleIn;
        initialScale = scaleIn;
        finalScale = scaleIn;
        return this;
    }

    /**
     * Sets the partical tint color (but not alpha) including removing
     * any tint animation.
     *
     * @param redIn the red in
     * @param greenIn the green in
     * @param blueIn the blue in
     * @return the custom particle
     */
    public ParticleCustom setTintColor(float redIn, float greenIn, float blueIn)
    {
        particleRed = redIn;
        particleGreen = greenIn;
        particleBlue = blueIn;
        initialTint = new Color(redIn / 256.0F, greenIn / 256.0F, blueIn / 256.0F);
        finalTint = initialTint;
        return this;
    }
    
    /**
     * Sets the partical tint color (but not alpha) including removing
     * any tint animation.
     *
     * @param colorIn the color in
     * @return the custom particle
     */
    public ParticleCustom setTintColor(Color colorIn)
    {
        particleGreen = colorIn.getGreen() / 256.0F;
        particleBlue = colorIn.getBlue() / 256.0F;
        particleRed = colorIn.getRed() / 256.0F;
        initialTint = colorIn;
        finalTint = initialTint;
        return this;
    }

    /**
     * Sets the partical tint color and alpha including removing
     * any tint and alpha animation.
     *
     * @param redIn the red in
     * @param greenIn the green in
     * @param blueIn the blue in
     * @param alphaIn the alpha in
     * @return the custom particle
     */
    public ParticleCustom setTintColorAndAlpha(float redIn, float greenIn, float blueIn, float alphaIn)
    {
        setTintColor(redIn, greenIn, blueIn);
        setAlpha(alphaIn);
        return this;
    }

    /**
     * Sets the partical tint color and alpha including removing
     * any tint and alpha animation.
     *
     * @param colorIn the color in
     * @return the custom particle
     */
    public ParticleCustom setTintColorAndAlpha(Color colorIn)
    {
        setTintColor(colorIn);
        setAlpha(colorIn);
        return this;
    }
    
    /**
     * Sets the partical alpha as well as removing
     * any alpha animation.
     *
     * @param alphaIn the alpha in
     * @return the custom particle
     */
    public ParticleCustom setAlpha(float alphaIn)
    {
        particleAlpha = alphaIn;
        initialAlpha = alphaIn;
        finalAlpha = initialAlpha;
        return this;
    }
    
    /**
     * Sets the partical alpha as well as removing
     * any alpha animation.
     *
     * @param colorIn the color in
     * @return the custom particle
     */
    public ParticleCustom setAlpha(Color colorIn)
    {
        particleAlpha = colorIn.getAlpha() / 256.0F;
        initialAlpha = particleAlpha;
        finalAlpha = initialAlpha;
        return this;
    }
    
    /**
     * Set initial tint (for tint animation) as well as making
     * current tint equal to intial tint (meaning this method should
     * be called during particle creation).
     *
     * @param colorIn the color in
     * @return the custom particle
     */
    public ParticleCustom setInitalTint(Color colorIn)
    {
        initialTint = colorIn;
        particleGreen = colorIn.getGreen() / 256.0F;
        particleBlue = colorIn.getBlue() / 256.0F;
        particleRed = colorIn.getRed() / 256.0F;
        return this;
    }
    
    /**
     * Set final tint color (for tint animation). If it is different than
     * the initialTint value animation will automatically be enabled.
     *
     * @param colorIn the color in
     * @return the custom particle
     */
    public ParticleCustom setFinalTint(Color colorIn)
    {
        finalTint = colorIn;
        return this;
    }

    /**
     * Set initial alpha (for alpha animation) as well as making
     * current alpha equal to intial alpha (meaning this method should
     * be called during particle creation).
     *
     * @param alphaIn the alpha in
     * @return the custom particle
     */
    public ParticleCustom setInitialAlpha(float alphaIn)
    {
        initialAlpha = alphaIn;
        particleAlpha = alphaIn;
        return this;
    }
    
    /**
     * Set initial alpha (for alpha animation) as well as making
     * current alpha equal to intial alpha (meaning this method should
     * be called during particle creation).
     *
     * @param colorIn the color in
     * @return the custom particle
     */
    public ParticleCustom setInitialAlpha(Color colorIn)
    {
        initialAlpha = colorIn.getAlpha() / 256.0F;
        particleAlpha = colorIn.getAlpha() / 256.0F;
        return this;
    }
    
    /**
     * Set final alpha (for alpha animation). If it is different than
     * the initialAlpha value animation will automatically be enabled.
     *
     * @param alphaIn the alpha in
     * @return the custom particle
     */
    public ParticleCustom setFinalAlpha(float alphaIn)
    {
        finalAlpha = alphaIn;
        return this;
    }

    /**
     * Set final alpha (for alpha animation). If it is different than
     * the initialAlpha value animation will automatically be enabled.
     *
     * @param colorIn the color in
     * @return the custom particle
     */
    public ParticleCustom setFinalAlpha(Color colorIn)
    {
        finalAlpha = colorIn.getAlpha() / 256.0F;
        return this;
    }
    
    /**
     * Set initial scale (for scale animation) as well as making
     * current scale equal to intial scale (meaning this method should
     * be called during particle creation).
     *
     * @param scaleIn the scale in
     * @return the custom particle
     */
    public ParticleCustom setInitialScale(float scaleIn)
    {
        particleScale = scaleIn;
        initialScale = scaleIn;
        return this;
    }
    
    /**
     * Set final scale (for scale animation). If it is different than
     * the initialScale value animation will automatically be enabled.
     *
     * @param scaleIn the scale in
     * @return the custom particle
     */
    public ParticleCustom setFinalScale(float scaleIn)
    {
        finalScale = scaleIn;
        return this;
    }
   
    /**
     * Sets the gravity.
     *
     * @param gravityIn the gravity in
     * @return the custom particle
     */
    public ParticleCustom setGravity(float gravityIn)
    {
        particleGravity = gravityIn;
        return this;
    }
    
    /**
     * Sets the rot speed.
     *
     * @param rotIn the rot in
     * @return the custom particle
     */
    public ParticleCustom setRotSpeed(float rotIn)
    {
        rotSpeed = rotIn;
        return this;
    }
    
    /**
     * Sets the enable depth.
     *
     * @param enableDepthIn the enable depth in
     * @return the custom particle
     */
    public ParticleCustom setEnableDepth(boolean enableDepthIn)
    {
        enableDepth = enableDepthIn;
        return this;
    }

    /**
     * Defines a portion of an image file as the texture for the 
     * custom particle, as well as indicating if the image represents
     * an animation "strip" (multiple frames in the v dimension).
     * 
     * @author jabelar
     *
     */
    public static class TextureDefinition
    {
        private String name;
        private ResourceLocation resourceLocation;
        private float uMin;
        private float vMin;
        private float uMax;
        private float vMax;
        private boolean tweenAnimationMode;
        private int animationFrameCount;
       
       /**
        * Instantiates a new texture definition. It assumes that the image file
        * is at a file location in /textures/particles/ folder in your mod assets.
        *
        * @param parName the name
        */
       public TextureDefinition(String parName)
        {
            this(parName, 0.0F, 0.0F, 1.0F, 1.0F);
        }

        private TextureDefinition(String parName, float parUMin, float parVMin, float parUMax, float parVMax)
        {
            this(parName, parUMin, parVMin, parUMax, parVMax, false, 1);
        }

        /**
        * Instantiates a new texture definition. It assumes that the image file
        * is at a file location in /textures/particles/ folder in your mod assets.
         *
         * @param parName the name
         * @param parAnimMode the animation mode
         * @param parAnimFrames the animation frames
         */
        public TextureDefinition(String parName, boolean parAnimMode, int parAnimFrames)
        {
            this(parName, 0.0F, 0.0F, 1.0F, 1.0F / parAnimFrames, parAnimMode, parAnimFrames);
        }

        private TextureDefinition(String parName, float parUMin, float parVMin, float parUMax, float parVMax, boolean parAnimMode, int parAnimFrames)
        {
            name = parName;
            resourceLocation = new ResourceLocation(MainMod.MODID, "textures/particles/"+name+".png");
            uMin = parUMin;
            vMin = parVMin;
            uMax = parUMax;
            vMax = parVMax;
            tweenAnimationMode = parAnimMode;
            animationFrameCount = parAnimFrames;
        }

        /* 
         * Getter methods.
         */
        
        /**
         * Gets the name.
         *
         * @return the name
         */
        public String getName() { return name; }
        
        /**
         * Gets the resource location.
         *
         * @return the resource location
         */
        public ResourceLocation getResourceLocation() { return resourceLocation; }
        
        /**
         * Gets the u min.
         *
         * @return the u min
         */
        public float getUMin() { return uMin; }
        
        /**
         * Gets the v min.
         *
         * @return the v min
         */
        public float getVmin() { return vMin; }
        
        /**
         * Gets the u max.
         *
         * @return the u max
         */
        public float getUMax() { return uMax; }
        
        /**
         * Gets the v max.
         *
         * @return the v max
         */
        public float getVMax() { return vMax; }
        
        /**
         * Checks if is tween animation mode.
         *
         * @return true, if is tween animation mode
         */
        public boolean isTweenAnimationMode() { return tweenAnimationMode; }
        
        /**
         * Gets the animation frame count.
         *
         * @return the animation frame count
         */
        public int getAnimationFrameCount() { return animationFrameCount; }
    }
}
