package com.blogspot.jabelarminecraft.examplemod.client.renderers;

import net.minecraft.client.model.ModelArmorStandArmor;
import net.minecraft.client.renderer.entity.RenderArmorStand;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModRenderArmorStand extends RenderArmorStand
{
    public ModRenderArmorStand(RenderManager manager)
    {
        super(manager);

        ModLayerBipedArmor layerbipedarmor = new ModLayerBipedArmor(this)
        {
            @Override
            protected void initArmor()
            {
                modelLeggings = new ModelArmorStandArmor(0.5F);
                modelArmor = new ModelArmorStandArmor(1.0F);
            }
        };
        addLayer(layerbipedarmor);
        // DEBUG
        System.out.println("ModRenderArmorStand layerRenderers = "+layerRenderers);
    }

}
