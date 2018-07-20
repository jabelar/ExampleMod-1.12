package com.blogspot.jabelarminecraft.examplemod.client.renderers;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPigZombie;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModRenderPigZombie extends RenderPigZombie
{

    public ModRenderPigZombie(RenderManager renderManagerIn)
    {
        super(renderManagerIn);

        layerRenderers.remove(3);
        addLayer(new ModLayerBipedArmor(this)
        {
            @Override
            protected void initArmor()
            {
                modelLeggings = new ModelZombie(0.5F, true);
                modelArmor = new ModelZombie(1.0F, true);
            }
        });
        // DEBUG
        System.out.println("ModRenderPigZombie layerRenderers = "+layerRenderers);
    }
}
