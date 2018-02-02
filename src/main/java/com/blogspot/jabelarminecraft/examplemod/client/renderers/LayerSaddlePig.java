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
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// TODO: Auto-generated Javadoc
@SideOnly(Side.CLIENT)
public class LayerSaddlePig implements LayerRenderer<EntityPigTest>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/pig/pig_saddle.png");
    private final RenderPigTest pigRenderer;
    private final ModelPig pigModel = new ModelPig(0.5F);

    /**
     * Instantiates a new layer saddle pig.
     *
     * @param renderPigTest the render pig test
     */
    public LayerSaddlePig(RenderPigTest renderPigTest)
    {
        this.pigRenderer = renderPigTest;
    }

    /* (non-Javadoc)
     * @see net.minecraft.client.renderer.entity.layers.LayerRenderer#doRenderLayer(net.minecraft.entity.EntityLivingBase, float, float, float, float, float, float, float)
     */
    @Override
    public void doRenderLayer(EntityPigTest entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if (entitylivingbaseIn.getSaddled())
        {
            this.pigRenderer.bindTexture(TEXTURE);
            this.pigModel.setModelAttributes(this.pigRenderer.getMainModel());
            this.pigModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    /* (non-Javadoc)
     * @see net.minecraft.client.renderer.entity.layers.LayerRenderer#shouldCombineTextures()
     */
    @Override
    public boolean shouldCombineTextures()
    {
        return false;
    }
}