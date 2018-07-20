package com.blogspot.jabelarminecraft.examplemod.client.renderers;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.util.ResourceLocation;

public class ModRenderWitherSkeleton extends ModRenderSkeleton
{
    private static final ResourceLocation WITHER_SKELETON_TEXTURES = new ResourceLocation("textures/entity/skeleton/wither_skeleton.png");

    public ModRenderWitherSkeleton(RenderManager manager)
    {
        super(manager);
        // DEBUG
        System.out.println("ModRenderWitherSkeleton layerRenderers = "+layerRenderers);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(AbstractSkeleton entity)
    {
        return WITHER_SKELETON_TEXTURES;
    }

    /**
     * Allows the render to do state modifications necessary before the model is rendered.
     */
    @Override
    protected void preRenderCallback(AbstractSkeleton entitylivingbaseIn, float partialTickTime)
    {
        GlStateManager.scale(1.2F, 1.2F, 1.2F);
    }
}
