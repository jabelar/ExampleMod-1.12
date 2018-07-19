package com.blogspot.jabelarminecraft.examplemod.client.renderers;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;

public class ModRenderPlayer extends RenderPlayer
{

    public ModRenderPlayer(RenderManager renderManager)
    {
        super(renderManager);
        // DEBUG
        System.out.println("Replacing RenderPlayer with custom version");
        
        this.addLayer(new ModLayerBipedArmor(this));
    }

    public ModRenderPlayer(RenderManager renderManager, boolean b)
    {
        super(renderManager, b);
    }

}
