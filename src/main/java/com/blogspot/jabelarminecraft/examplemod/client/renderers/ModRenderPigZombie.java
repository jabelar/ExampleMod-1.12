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

//        this.layerRenderers.remove(1);
        this.addLayer(new ModLayerBipedArmor(this)
        {
            @Override
            protected void initArmor()
            {
                this.modelLeggings = new ModelZombie(0.5F, true);
                this.modelArmor = new ModelZombie(1.0F, true);
            }
        });
    }
}
