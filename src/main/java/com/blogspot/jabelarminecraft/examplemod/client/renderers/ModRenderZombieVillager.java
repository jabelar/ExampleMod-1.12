package com.blogspot.jabelarminecraft.examplemod.client.renderers;

import net.minecraft.client.model.ModelZombieVillager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderZombieVillager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModRenderZombieVillager extends RenderZombieVillager
{

    public ModRenderZombieVillager(RenderManager manager)
    {
        super(manager);
        ModLayerBipedArmor layerbipedarmor = new ModLayerBipedArmor(this)
        {
            @Override
            protected void initArmor()
            {
                modelLeggings = new ModelZombieVillager(0.5F, 0.0F, true);
                modelArmor = new ModelZombieVillager(1.0F, 0.0F, true);
            }
        };
        layerRenderers.remove(3);
        addLayer(layerbipedarmor);
        // DEBUG
        System.out.println("ModRenderZombieVillager layerRenderers = "+layerRenderers);
    }
}
