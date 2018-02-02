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

import net.minecraft.client.model.ModelPig;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// TODO: Auto-generated Javadoc
@SideOnly(Side.CLIENT)
public class RenderPigTest extends RenderLiving<EntityPigTest>
{
    private static final ResourceLocation PIG_TEXTURES = new ResourceLocation("textures/entity/pig/pig.png");

    /**
     * Instantiates a new render pig test.
     *
     * @param p_i47198_1_ the p i 47198 1
     */
    public RenderPigTest(RenderManager p_i47198_1_)
    {
        super(p_i47198_1_, new ModelPig(), 0.7F);
        this.addLayer(new LayerSaddlePig(this));
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     *
     * @param entity the entity
     * @return the entity texture
     */
    @Override
    protected ResourceLocation getEntityTexture(EntityPigTest entity)
    {
        return PIG_TEXTURES;
    }
}