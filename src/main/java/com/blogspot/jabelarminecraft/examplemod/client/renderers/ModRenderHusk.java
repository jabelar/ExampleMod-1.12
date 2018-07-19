package com.blogspot.jabelarminecraft.examplemod.client.renderers;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;

public class ModRenderHusk extends ModRenderZombie
{
    private static final ResourceLocation HUSK_ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/husk.png");

    public ModRenderHusk(RenderManager p_i47204_1_)
    {
        super(p_i47204_1_);
    }

    /**
     * Allows the render to do state modifications necessary before the model is rendered.
     */
    protected void preRenderCallback(EntityZombie entitylivingbaseIn, float partialTickTime)
    {
        float f = 1.0625F;
        GlStateManager.scale(1.0625F, 1.0625F, 1.0625F);
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityZombie entity)
    {
        return HUSK_ZOMBIE_TEXTURES;
    }
}