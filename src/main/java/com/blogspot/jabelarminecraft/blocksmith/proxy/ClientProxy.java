/**
    Copyright (C) 2014 by jabelar

    This file is part of jabelar's Minecraft Forge modding examples; as such,
    you can redistribute it and/or modify it under the terms of the GNU
    General Public License as published by the Free Software Foundation,
    either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    For a copy of the GNU General Public License see <http://www.gnu.org/licenses/>.

    If you're interested in licensing the code under different terms you can
    contact the author at julian_abelar@hotmail.com 
*/

package com.blogspot.jabelarminecraft.blocksmith.proxy;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

import com.blogspot.jabelarminecraft.blocksmith.BlockSmith;
import com.blogspot.jabelarminecraft.blocksmith.VersionChecker;
import com.blogspot.jabelarminecraft.blocksmith.entities.EntityPigTest;
import com.blogspot.jabelarminecraft.blocksmith.registries.BlockRegistry;
import com.blogspot.jabelarminecraft.blocksmith.registries.ItemRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPig;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientProxy extends CommonProxy 
{
    /*
     * Fields related to key binding
     */
    public static KeyBinding[] keyBindings;
    
    /*
     * For rendering a sphere, need ids for call lists for outside and inside
     */
    public static int sphereIdOutside;
    public static int sphereIdInside;
    
    @Override
    public void fmlLifeCycleEvent(FMLPreInitializationEvent event)
    {
        // DEBUG
        System.out.println("on Client side");
        
        // do common stuff
        super.fmlLifeCycleEvent(event);

    }
    
    @Override
    public void fmlLifeCycleEvent(FMLInitializationEvent event)
    {
        // DEBUG
        System.out.println("on Client side");

        /*
         *  do common stuff
         */
        super.fmlLifeCycleEvent(event);

        /*
         *  do client-specific stuff
         */
        // register key bindings
        registerKeyBindings();

        // create sphere call list
        createSphereCallList();
        
        // register model loader
//        ModelLoaderRegistry.registerLoader(new MyModelLoader());

        // register renderers
        registerEntityRenderers();
        registerItemRenderers();
        registerBlockRenderers();
    }
    
    @Override
    public void fmlLifeCycleEvent(FMLPostInitializationEvent event)
    {
        // DEBUG
        System.out.println("on Client side");

        // do common stuff
        super.fmlLifeCycleEvent(event);

        // do client-specific stuff
        BlockSmith.versionChecker = new VersionChecker();
        Thread versionCheckThread = new Thread(BlockSmith.versionChecker, "Version Check");
        versionCheckThread.start();

}

    /*
     * Registers key bindings
     */
    public void registerKeyBindings() 
    {        
        // declare an array of key bindings
        keyBindings = new KeyBinding[2]; 
        
        // instantiate the key bindings
        keyBindings[0] = new KeyBinding("key.structure.desc", Keyboard.KEY_P, "key.magicbeans.category");
        keyBindings[1] = new KeyBinding("key.hud.desc", Keyboard.KEY_H, "key.magicbeans.category");
        
        // register all the key bindings
        for (int i = 0; i < keyBindings.length; ++i) 
        {
            ClientRegistry.registerKeyBinding(keyBindings[i]);
        }
    }

    /**
     * Registers the entity renderers
     */
    public void registerEntityRenderers() 
    {
        // the float parameter passed to the Render class is the shadow size for the entity
      
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        // RenderingRegistry.registerEntityRenderingHandler(EntityGoldenGoose.class, new RenderGoldenGoose(renderManager, new ModelGoldenGoose(), 0.5F)); // 0.5F is shadow size 
    	RenderingRegistry.registerEntityRenderingHandler(EntityPigTest.class, new RenderPig(renderManager));
    }
    
    public void registerItemRenderers()
    {
        // DEBUG
        System.out.println("Registering item renderers");
        
        registerItemRenderer(ItemRegistry.COW_HIDE);
        registerItemRenderer(ItemRegistry.SHEEP_SKIN);
        registerItemRenderer(ItemRegistry.PIG_SKIN);
        registerItemRenderer(ItemRegistry.HORSE_HIDE);
        registerItemRenderer(ItemRegistry.SWORD_EXTENDED);
        // registerItemRenderer(JnaeMod.magicBeans);
    }
    
    public void registerItemRenderer(Item parItem)
    {
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

        renderItem.getItemModelMesher().register(parItem, 0, new ModelResourceLocation(BlockSmith.MODID + ":" + parItem.getUnlocalizedName().substring(5), "inventory"));
    }
    
    public void registerBlockRenderers()
    {
        // DEBUG
        System.out.println("Registering block renderers");
        
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(BlockRegistry.TANNING_RACK), 0, new ModelResourceLocation(BlockSmith.MODID + ":" + BlockRegistry.TANNING_RACK.getUnlocalizedName().substring(5), "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(BlockRegistry.GRINDER), 0, new ModelResourceLocation(BlockSmith.MODID + ":" + BlockRegistry.GRINDER.getUnlocalizedName().substring(5), "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(BlockRegistry.COMPACTOR), 0, new ModelResourceLocation(BlockSmith.MODID + ":" + BlockRegistry.COMPACTOR.getUnlocalizedName().substring(5), "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(BlockRegistry.DECONSTRUCTOR), 0, new ModelResourceLocation(BlockSmith.MODID + ":" + BlockRegistry.DECONSTRUCTOR.getUnlocalizedName().substring(5), "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(BlockRegistry.FORGE), 0, new ModelResourceLocation(BlockSmith.MODID + ":" + BlockRegistry.FORGE.getUnlocalizedName().substring(5), "inventory"));
//        renderItem.getItemModelMesher().register(Item.getItemFromBlock(BlockSmith.blockForgeLit), 0, new ModelResourceLocation(BlockSmith.MODID + ":" + BlockSmith.blockForgeLit.getUnlocalizedName().substring(5), "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(BlockRegistry.MOVING_LIGHT_SOURCE), 0, new ModelResourceLocation("torch"));
    }
    
    /*     
     * Thanks to CoolAlias for this tip!
     */
    /**
     * Returns a side-appropriate EntityPlayer for use during message handling
     */
    @Override
    public EntityPlayer getPlayerEntityFromContext(MessageContext ctx) 
    {
        // Note that if you simply return 'Minecraft.getMinecraft().thePlayer',
        // your packets will not work because you will be getting a client
        // player even when you are on the server! Sounds absurd, but it's true.

        // Solution is to double-check side before returning the player:
        return (ctx.side.isClient() ? Minecraft.getMinecraft().player : super.getPlayerEntityFromContext(ctx));
    }
    
    /*
     * For rendering a sphere, need to make the call list
     * Must be called after pre-init, otherwise Minecraft.getMinecraft() will fail will null pointer exception
     */
    public void createSphereCallList()
    {
        Sphere sphere = new Sphere();
       //GLU_POINT will render it as dots.
       //GLU_LINE will render as wireframe
       //GLU_SILHOUETTE will render as ?shadowed? wireframe
       //GLU_FILL as a solid.
        sphere.setDrawStyle(GLU.GLU_FILL);
       //GLU_SMOOTH will try to smoothly apply lighting
       //GLU_FLAT will have a solid brightness per face, and will not shade.
       //GLU_NONE will be completely solid, and probably will have no depth to it's appearance.
        sphere.setNormals(GLU.GLU_SMOOTH);
       //GLU_INSIDE will render as if you are inside the sphere, making it appear inside out.(Similar to how ender portals are rendered)
        sphere.setOrientation(GLU.GLU_OUTSIDE);
        sphereIdOutside = GL11.glGenLists(1);
       //Create a new list to hold our sphere data.
        GL11.glNewList(sphereIdOutside, GL11.GL_COMPILE);
       //binds the texture 
       ResourceLocation rL = new ResourceLocation(BlockSmith.MODID+":textures/entities/sphere.png");
       Minecraft.getMinecraft().getTextureManager().bindTexture(rL);
       //The drawing the sphere is automatically doing is getting added to our list. Careful, the last 2 variables
       //control the detail, but have a massive impact on performance. 32x32 is a good balance on my machine.s
       sphere.draw(0.5F, 32, 32);
       GL11.glEndList();

       //GLU_INSIDE will render as if you are inside the sphere, making it appear inside out.(Similar to how ender portals are rendered)
       sphere.setOrientation(GLU.GLU_INSIDE);
       sphereIdInside = GL11.glGenLists(1);
       //Create a new list to hold our sphere data.
       GL11.glNewList(sphereIdInside, GL11.GL_COMPILE);
       Minecraft.getMinecraft().getTextureManager().bindTexture(rL);
       //The drawing the sphere is automatically doing is getting added to our list. Careful, the last 2 variables
       //control the detail, but have a massive impact on performance. 32x32 is a good balance on my machine.s
       sphere.draw(0.5F, 32, 32);
       GL11.glEndList();
    }
    
    /*
     * This initializes the List in the public field itemListFromRegistry with all possilbe
     * ItemStack types (all valid metadata values, as well as any NBT that is used to create variants
     * in mods like Tinker's Construct).
     */
//    @Override
//	protected void initItemStackRegistry()
//    {
//		itemStackRegistry.clear();
//		
//		for (Object theObj: Item.REGISTRY)
//		{
//			((Item)theObj).getSubItems((Item)theObj, null, itemStackRegistry); // this method directly appends to ItemStackRegistry
//		}
//		
//		// DEBUG
//		System.out.println("ItemStack registry = "+itemStackRegistry.toString());
//
//		return;
//    }
}