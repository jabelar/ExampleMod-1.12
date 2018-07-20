package com.blogspot.jabelarminecraft.examplemod.client.renderers;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderGiantZombie;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModRenderGiantZombie extends RenderGiantZombie
{

    public ModRenderGiantZombie(RenderManager manager, float scale)
    {
        super(manager, scale);
        ModLayerBipedArmor layerbipedarmor = new ModLayerBipedArmor(this)
        {
            @Override
            protected void initArmor()
            {
                modelLeggings = new ModelZombie(0.5F, true);
                modelArmor = new ModelZombie(1.0F, true);
            }
        };
        layerRenderers.remove(1);
        addLayer(layerbipedarmor);
        // DEBUG
        System.out.println("ModRenderGiantZombie layerRenderers = "+layerRenderers);
    }

}
