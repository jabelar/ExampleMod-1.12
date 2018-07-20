package com.blogspot.jabelarminecraft.examplemod.client.renderers;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerStrayClothing;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.util.ResourceLocation;

public class ModRenderStray extends ModRenderSkeleton
{
    private static final ResourceLocation STRAY_SKELETON_TEXTURES = new ResourceLocation("textures/entity/skeleton/stray.png");

    public ModRenderStray(RenderManager manager)
    {
        super(manager);
        addLayer(new LayerStrayClothing(this));
        // DEBUG
        System.out.println("ModRenderStray layerRenderers = "+layerRenderers);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(AbstractSkeleton entity)
    {
        return STRAY_SKELETON_TEXTURES;
    }
}