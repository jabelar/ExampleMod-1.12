package com.blogspot.jabelarminecraft.examplemod.client.renderers;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModRenderPlayer extends RenderPlayer
{

    public ModRenderPlayer(RenderManager renderManager)
    {
        this(renderManager, false);
    }

    public ModRenderPlayer(RenderManager renderManager, boolean b)
    {
        super(renderManager, b);
        
        // DEBUG
        System.out.println("Replacing RenderPlayer with custom version, skinny version = : "+b);
        
        layerRenderers.remove(0);
        addLayer(new ModLayerBipedArmor(this));
   }

}
