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
package com.blogspot.jabelarminecraft.examplemod.client.renderers;

import com.blogspot.jabelarminecraft.examplemod.entities.EntityPigTest;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

// TODO: Auto-generated Javadoc
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
    
        /* (non-Javadoc)
         * @see net.minecraftforge.fml.client.registry.IRenderFactory#createRenderFor(net.minecraft.client.renderer.entity.RenderManager)
         */
        @Override
        public Render<EntityPigTest> createRenderFor(RenderManager manager)
        {
            return new RenderPigTest(manager);
        }
    }
}
