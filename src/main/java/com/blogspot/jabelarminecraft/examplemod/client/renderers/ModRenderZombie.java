package com.blogspot.jabelarminecraft.examplemod.client.renderers;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderZombie;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModRenderZombie extends RenderZombie
{

    public ModRenderZombie(RenderManager renderManagerIn)
    {
        super(renderManagerIn);

        layerRenderers.remove(3);
        ModLayerBipedArmor layerbipedarmor = new ModLayerBipedArmor(this)
        {
            @Override
            protected void initArmor()
            {
                modelLeggings = new ModelZombie(0.5F, true);
                modelArmor = new ModelZombie(1.0F, true);
            }
        };
        addLayer(layerbipedarmor);
        // DEBUG
        System.out.println("ModRenderZombie layerRenderers = "+layerRenderers);
    }
}
