package com.blogspot.jabelarminecraft.examplemod.client.renderers;

import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModRenderSkeleton extends RenderSkeleton
{

    public ModRenderSkeleton(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
        
        layerRenderers.remove(4);
        addLayer(new ModLayerBipedArmor(this)
        {
            @Override
            protected void initArmor()
            {
                modelLeggings = new ModelSkeleton(0.5F, true);
                modelArmor = new ModelSkeleton(1.0F, true);
            }
        });
         // DEBUG
        System.out.println("ModRenderSkeleton layerRenderers = "+layerRenderers);
   }
}
