package com.blogspot.jabelarminecraft.examplemod.client.renderers;

import com.blogspot.jabelarminecraft.examplemod.entities.EntityPigTest;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class RenderFactories
{
    /**
     * Registers the entity renderers.
     */
    public static void registerEntityRenderers()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityPigTest.class, RenderFactoryEntityPigTest.INSTANCE);
    }

    public static class RenderFactoryEntityPigTest implements IRenderFactory<EntityPigTest>
    {
        public final static RenderFactoryEntityPigTest INSTANCE = new RenderFactoryEntityPigTest();
    
        @Override
        public Render<EntityPigTest> createRenderFor(RenderManager manager)
        {
            return new RenderPigTest(manager);
        }
    }
}
