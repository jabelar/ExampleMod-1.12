/**
    Copyright (C) 2017 by jabelar

    This file is part of jabelar's Minecraft Forge modding examples; as such,
    you can redistribute it and/or modify it under the terms of the GNU
    General Public License as published by the Free Software Foundation,
    either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    For a copy of the GNU General Public License see <http://www.gnu.org/licenses/>.
*/

package com.blogspot.jabelarminecraft.examplemod.proxy;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

import com.blogspot.jabelarminecraft.examplemod.MainMod;
import com.blogspot.jabelarminecraft.examplemod.blocks.BlockLeavesCloud;
import com.blogspot.jabelarminecraft.examplemod.client.gui.GuiCreateWorldMod;
import com.blogspot.jabelarminecraft.examplemod.client.localization.ModLocale;
import com.blogspot.jabelarminecraft.examplemod.client.renderers.ModRenderArmorStand;
import com.blogspot.jabelarminecraft.examplemod.client.renderers.ModRenderGiantZombie;
import com.blogspot.jabelarminecraft.examplemod.client.renderers.ModRenderItem;
import com.blogspot.jabelarminecraft.examplemod.client.renderers.ModRenderPlayer;
import com.blogspot.jabelarminecraft.examplemod.client.renderers.ModRenderSkeleton;
import com.blogspot.jabelarminecraft.examplemod.client.renderers.ModRenderZombie;
import com.blogspot.jabelarminecraft.examplemod.client.renderers.ModRenderZombieVillager;
import com.blogspot.jabelarminecraft.examplemod.client.renderers.RenderFactories;
import com.blogspot.jabelarminecraft.examplemod.init.ModBlockColors;
import com.blogspot.jabelarminecraft.examplemod.init.ModKeyBindings;
import com.blogspot.jabelarminecraft.examplemod.init.ModMaterials;
import com.blogspot.jabelarminecraft.examplemod.init.ModNetworking;
import com.blogspot.jabelarminecraft.examplemod.init.ModWorldGen;
import com.blogspot.jabelarminecraft.examplemod.items.IExtendedReach;
import com.blogspot.jabelarminecraft.examplemod.networking.MessageExtendedReachAttack;
import com.blogspot.jabelarminecraft.examplemod.utilities.Utilities;
import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.LanguageMap;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;


// TODO: Auto-generated Javadoc
@EventBusSubscriber(value = Side.CLIENT, modid = MainMod.MODID)
public class ClientProxy implements IProxy
{
    /*
     * For rendering a sphere, need ids for call lists for outside and inside
     */
    public static int sphereIdOutside;
    public static int sphereIdInside;
    
    public static MouseHelper mouseHelperAI; // used to intercept user mouse movement for "bot" functionality

    public static ModRenderItem modRenderItem; // used to provide custom enchantment glint color
    public static Field modelManager = ReflectionHelper.findField(Minecraft.class, "modelManager", "modelManager");
    public static Field renderItem = ReflectionHelper.findField(Minecraft.class, "renderItem", "renderItem");
    public static Field itemRenderer = ReflectionHelper.findField(ItemRenderer.class, "itemRenderer", "itemRenderer");
    public static Field playerRenderer = ReflectionHelper.findField(RenderManager.class, "playerRender", "playerRenderer");
    public static Field skinMap = ReflectionHelper.findField(RenderManager.class, "skinMap", "skinMap");
    
    public static Field locale = ReflectionHelper.findField(LanguageManager.class, "CURRENT_LOCALE", "CURRENT_LOCALE");
    public static ModLocale MOD_LOCALE = new ModLocale();

    /* (non-Javadoc)
     * @see com.blogspot.jabelarminecraft.examplemod.proxy.IProxy#preInit(net.minecraftforge.fml.common.event.FMLPreInitializationEvent)
     */
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        // DEBUG
        System.out.println("on Client side");
        
        Minecraft mc = Minecraft.getMinecraft();
        
        // Replace mouse helper with custom version
        mouseHelperAI = new MouseHelperAI();
        mc.mouseHelper = mouseHelperAI;
                
        RenderFactories.registerEntityRenderers();
        
        fixLocaleClass();
    }
    
    /**
     * Fix locale class.
     */
    public void fixLocaleClass()
    {
        // DEBUG
        System.out.println("Swapping in custom locale class");
        Field modifiersField;
        try
        {
            modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            try
            {
                modifiersField.setInt(locale, locale.getModifiers() & ~Modifier.FINAL);
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        catch (NoSuchFieldException | SecurityException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try
        {
            locale.set(null, MOD_LOCALE);
        }
        catch (IllegalArgumentException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /* (non-Javadoc)
     * @see com.blogspot.jabelarminecraft.examplemod.proxy.IProxy#init(net.minecraftforge.fml.common.event.FMLInitializationEvent)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void init(FMLInitializationEvent event)
    {
        // DEBUG
        System.out.println("on Client side");

        ModKeyBindings.registerKeyBindings();
        ModBlockColors.registerBlockColors();
        
        Minecraft mc = Minecraft.getMinecraft();
        
        // Replace render item with custom version
        modelManager.setAccessible(true);
        renderItem.setAccessible(true);
        playerRenderer.setAccessible(true);
        try
        {
            modRenderItem = new ModRenderItem(mc.getTextureManager(), (ModelManager) modelManager.get(mc), mc.getItemColors(), ((RenderItem)renderItem.get(mc)).getItemModelMesher());
            renderItem.set(mc, modRenderItem);
            itemRenderer.set(mc.getItemRenderer(), modRenderItem);
            // DEBUG
            System.out.println("playerRenderer before reflection is "+playerRenderer.get(mc.getRenderManager()));
            playerRenderer.set(mc.getRenderManager(), new ModRenderPlayer(mc.getRenderManager()));
            // DEBUG
            System.out.println("playerRenderer after reflection is "+playerRenderer.get(mc.getRenderManager()));
            ((Map<String, RenderPlayer>)skinMap.get(mc.getRenderManager())).put("default", new ModRenderPlayer(mc.getRenderManager()));
            ((Map<String, RenderPlayer>)skinMap.get(mc.getRenderManager())).put("slim", new ModRenderPlayer(mc.getRenderManager(), true));
        }
        catch (IllegalArgumentException | IllegalAccessException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        mc.getRenderManager().entityRenderMap.put(EntityItem.class, new RenderEntityItem(mc.getRenderManager(), modRenderItem));
        mc.getRenderManager().entityRenderMap.put(EntitySkeleton.class, new ModRenderSkeleton(mc.getRenderManager()));
        mc.getRenderManager().entityRenderMap.put(EntityZombie.class, new ModRenderZombie(mc.getRenderManager()));
        mc.getRenderManager().entityRenderMap.put(EntityZombieVillager.class, new ModRenderZombieVillager(mc.getRenderManager()));
        mc.getRenderManager().entityRenderMap.put(EntityGiantZombie.class, new ModRenderGiantZombie(mc.getRenderManager(), 6.0F));
        mc.getRenderManager().entityRenderMap.put(EntityArmorStand.class, new ModRenderArmorStand(mc.getRenderManager()));
        
        // create sphere call list
        createSphereCallList();
    }

    /* (non-Javadoc)
     * @see com.blogspot.jabelarminecraft.examplemod.proxy.IProxy#postInit(net.minecraftforge.fml.common.event.FMLPostInitializationEvent)
     */
    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        // DEBUG
        System.out.println("on Client side");

        refreshLangResources();
    }

    /**
     * Refresh lang resources.
     */
    @SuppressWarnings("unlikely-arg-type")
    public void refreshLangResources()
    {
        // DEBUG
        System.out.println("Refreshing lang files with proper precedence");
//        Minecraft.getMinecraft().refreshResources();
        List<String> list = Lists.newArrayList("en_us");

        if (!"en_us".equals(Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage()))
        {
            list.add(Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().toString());
        }

        // This is a fix for problem where lang files are not properly replaced by resource packs
        MOD_LOCALE.loadLocaleDataFiles(Minecraft.getMinecraft().getResourceManager(), list);
        LanguageMap.replaceWith(MOD_LOCALE.properties);
    }

    /* (non-Javadoc)
     * @see com.blogspot.jabelarminecraft.examplemod.proxy.IProxy#getPlayerEntityFromContext(net.minecraftforge.fml.common.network.simpleimpl.MessageContext)
     */
    @Override
    public EntityPlayer getPlayerEntityFromContext(MessageContext ctx)
    {
        // Note that if you simply return 'Minecraft.getMinecraft().thePlayer',
        // your packets will not work because you will be getting a client
        // player even when you are on the server! Sounds absurd, but it's true.

        // Solution is to double-check side before returning the player:
        return (ctx.side.isClient() ? Minecraft.getMinecraft().player : ctx.getServerHandler().player);
    }

    /**
     * For rendering a sphere, need to make the call list Must be called after pre-init, otherwise Minecraft.getMinecraft() will fail will null pointer exception
     */
    public static void createSphereCallList()
    {
        Sphere sphere = new Sphere();
        // GLU_POINT will render it as dots.
        // GLU_LINE will render as wireframe
        // GLU_SILHOUETTE will render as ?shadowed? wireframe
        // GLU_FILL as a solid.
        sphere.setDrawStyle(GLU.GLU_FILL);
        // GLU_SMOOTH will try to smoothly apply lighting
        // GLU_FLAT will have a solid brightness per face, and will not shade.
        // GLU_NONE will be completely solid, and probably will have no depth to it's appearance.
        sphere.setNormals(GLU.GLU_SMOOTH);
        // GLU_INSIDE will render as if you are inside the sphere, making it appear inside out.(Similar to how ender portals are rendered)
        sphere.setOrientation(GLU.GLU_OUTSIDE);
        sphereIdOutside = GL11.glGenLists(1);
        // Create a new list to hold our sphere data.
        GL11.glNewList(sphereIdOutside, GL11.GL_COMPILE);
        // binds the texture
        ResourceLocation rL = new ResourceLocation(MainMod.MODID + ":textures/entities/sphere.png");
        Minecraft.getMinecraft().getTextureManager().bindTexture(rL);
        // The drawing the sphere is automatically doing is getting added to our list. Careful, the last 2 variables
        // control the detail, but have a massive impact on performance. 32x32 is a good balance on my machine.s
        sphere.draw(0.5F, 32, 32);
        GL11.glEndList();

        // GLU_INSIDE will render as if you are inside the sphere, making it appear inside out.(Similar to how ender portals are rendered)
        sphere.setOrientation(GLU.GLU_INSIDE);
        sphereIdInside = GL11.glGenLists(1);
        // Create a new list to hold our sphere data.
        GL11.glNewList(sphereIdInside, GL11.GL_COMPILE);
        Minecraft.getMinecraft().getTextureManager().bindTexture(rL);
        // The drawing the sphere is automatically doing is getting added to our list. Careful, the last 2 variables
        // control the detail, but have a massive impact on performance. 32x32 is a good balance on my machine.s
        sphere.draw(0.5F, 32, 32);
        GL11.glEndList();
    }

    /* (non-Javadoc)
     * @see com.blogspot.jabelarminecraft.examplemod.proxy.IProxy#handleMaterialAcceleration(net.minecraft.entity.Entity, net.minecraft.block.material.Material)
     */
    @Override
    public boolean handleMaterialAcceleration(Entity entityIn, Material materialIn)
    {
        World parWorld = entityIn.world;
        AxisAlignedBB bb = entityIn.getEntityBoundingBox().grow(0.0D, -0.4000000059604645D, 0.0D).shrink(0.001D);

        int j2 = MathHelper.floor(bb.minX);
        int k2 = MathHelper.ceil(bb.maxX);
        int l2 = MathHelper.floor(bb.minY);
        int i3 = MathHelper.ceil(bb.maxY);
        int j3 = MathHelper.floor(bb.minZ);
        int k3 = MathHelper.ceil(bb.maxZ);

        boolean flag = false;
        Vec3d vec3d = Vec3d.ZERO;
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

        for (int l3 = j2; l3 < k2; ++l3)
        {
            for (int i4 = l2; i4 < i3; ++i4)
            {
                for (int j4 = j3; j4 < k3; ++j4)
                {
                    blockpos$pooledmutableblockpos.setPos(l3, i4, j4);
                    IBlockState iblockstate1 = parWorld.getBlockState(blockpos$pooledmutableblockpos);
                    Block block = iblockstate1.getBlock();

                    Boolean result = block.isEntityInsideMaterial(parWorld, blockpos$pooledmutableblockpos, iblockstate1, entityIn, i3, materialIn, false);
                    if (result != null && result == true)
                    {
                        // Forge: When requested call blocks modifyAcceleration method, and more importantly cause this method to return true, which results in an entity being
                        // "inWater"
                        flag = true;
                        vec3d = block.modifyAcceleration(parWorld, blockpos$pooledmutableblockpos, entityIn, vec3d);

                        // // DEBUG
                        // System.out.println("Entity is inside material = "+materialIn+" and motion add vector = "+vec3d);

                        continue;
                    }
                    else if (result != null && result == false)
                        continue;

                    if (iblockstate1.getMaterial() == materialIn)
                    {
                        // // DEBUG
                        // System.out.println("blockstate material matches material in");

                        double d0 = i4 + 1 - BlockLiquid.getLiquidHeightPercent(iblockstate1.getValue(BlockLiquid.LEVEL).intValue());

                        if (i3 >= d0)
                        {
                            flag = true;
                            vec3d = block.modifyAcceleration(parWorld, blockpos$pooledmutableblockpos, entityIn, vec3d);

                            // // DEBUG
                            // System.out.println("deep enough to push entity and motion add = "+vec3d);
                        }
                    }
                }
            }
        }

        blockpos$pooledmutableblockpos.release();

        if (vec3d.lengthVector() > 0.0D && entityIn.isPushedByWater())
        {
            // // DEBUG
            // System.out.println("motion vector is non-zero");

            /*
             * Although applied to all entities, EntityPlayer doesn't really take affect, so the fluid motion control is handled in the client-side PlayerTickEvent
             */
            vec3d = vec3d.normalize();
            double d1 = 0.014D;
            entityIn.motionX += vec3d.x * d1;
            entityIn.motionY += vec3d.y * d1;
            entityIn.motionZ += vec3d.z * d1;
        }
        else
        {
            // // DEBUG
            // System.out.println("motion vector is zero");
        }

        entityIn.fallDistance = 0.0F;

        return flag;
    }

    /* (non-Javadoc)
     * @see com.blogspot.jabelarminecraft.examplemod.proxy.IProxy#serverStarting(net.minecraftforge.fml.common.event.FMLServerStartingEvent)
     */
    @Override
    public void serverStarting(FMLServerStartingEvent event)
    {
        // This will never get called on client side
    }

    /* (non-Javadoc)
     * @see com.blogspot.jabelarminecraft.examplemod.proxy.IProxy#setGraphicsLevel(com.blogspot.jabelarminecraft.examplemod.blocks.BlockLeavesCloud, boolean)
     */
    @Override
    public void setGraphicsLevel(BlockLeavesCloud parBlock, boolean parFancyEnabled)
    {
        parBlock.setGraphicsLevel(parFancyEnabled);
    }

    public class MouseHelperAI extends MouseHelper
    {
        
        /**
         * Instantiates a new mouse helper AI.
         */
        public MouseHelperAI()
        {
            super();
            // DEBUG
            System.out.println("Constructing MouseHelper for AI bots");
        }
        
        /* (non-Javadoc)
         * @see net.minecraft.util.MouseHelper#mouseXYChange()
         */
        @Override
        public void mouseXYChange()
        {
            if (Keyboard.isKeyDown(Keyboard.KEY_COMMA))
            {
                deltaX += 1;
            }
            else
            {
                deltaX = Mouse.getDX();
            }
            deltaY = Mouse.getDY();
        }
    }
    
    /**
     * Render the air indicator when submerged in a liquid.
     *
     * @param event the event
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public static void onEvent(RenderGameOverlayEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        GuiIngame ingameGUI = mc.ingameGUI;
        ScaledResolution scaledRes = event.getResolution();

        if (mc.getRenderViewEntity() instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer) mc.getRenderViewEntity();

            int airIndicatorX = scaledRes.getScaledWidth() / 2 + 91;
            int airIndicatorBottom = scaledRes.getScaledHeight() - 39;
            int airIndicatorTop = airIndicatorBottom - 10;

            if (entityplayer.isInsideOfMaterial(Material.WATER) || entityplayer.isInsideOfMaterial(ModMaterials.SLIME))
            {
                int airAmount = mc.player.getAir();
                int airLostPercent = MathHelper.ceil((airAmount - 2) * 10.0D / 300.0D);
                int airLeftPercent = MathHelper.ceil(airAmount * 10.0D / 300.0D) - airLostPercent;

                for (int airUnitIndex = 0; airUnitIndex < airLostPercent + airLeftPercent; ++airUnitIndex)
                {
                    if (airUnitIndex < airLostPercent)
                    {
                        ingameGUI.drawTexturedModalRect(airIndicatorX - airUnitIndex * 8 - 9, airIndicatorTop, 16, 18, 9, 9);
                    }
                    else
                    {
                        ingameGUI.drawTexturedModalRect(airIndicatorX - airUnitIndex * 8 - 9, airIndicatorTop, 25, 18, 9, 9);
                    }
                }
            }
        }
    }

    /**
     * Use fog density to create the effect of being under custom fluid, similar to how being under water does it.
     *
     * @param event the event
     */
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public static void onEvent(FogDensity event)
    {
        // EntityPlayer thePlayer = Minecraft.getMinecraft().player;
        if (event.getEntity().dimension == ModWorldGen.CLOUD_DIM_ID)
        {
            event.setDensity((float) Math.abs(Math.pow(((event.getEntity().posY-63)/(255-63)),4)));
        }
        event.setDensity((float) Math.abs(Math.pow(((event.getEntity().posY-63)/(255-63)),4)));
        
        if (event.getEntity().isInsideOfMaterial(ModMaterials.SLIME))
        {
            event.setDensity(0.5F);
        }
        else
        {
            event.setDensity(0.0001F);
        }

        event.setCanceled(true); // must cancel event for event handler to take effect
    }
    
    /**
     * On event.
     *
     * @param event the event
     */
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public static void onEvent(FogColors event)
    {
        event.setRed(0xFF);
        event.setGreen(0xFF);
        event.setBlue(0xFF);
    }
    
    /**
     * On event.
     *
     * @param event the event
     */
    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
    public static void onEvent(KeyInputEvent event)
    {
//        // DEBUG
//        System.out.println("Key Input Event");

        // make local copy of key binding array
        KeyBinding[] keyBindings = ModKeyBindings.keyBindings;
       
        // check each enumerated key binding type for pressed and take appropriate action
        if (keyBindings[0].isPressed()) 
        {
            // DEBUG
            System.out.println("Key binding ="+keyBindings[0].getKeyDescription());
                
            // do stuff for this key binding here
            // remember you may need to send packet to server
        }
        if (keyBindings[1].isPressed()) 
        {
            // DEBUG
            System.out.println("Key binding ="+keyBindings[1].getKeyDescription());
                
            // do stuff for this key binding here
            // remember you may need to send packet to server
        }
    }    

//    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
//    public static void onEvent(InputEvent.MouseInputEvent event)
//    {
//        Minecraft mc = Minecraft.getMinecraft();
//        if (mc.gameSettings.keyBindAttack.isPressed())
//        {
//            KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
//        }
//    }
    
    /**
     * Process an extended reach weapon.
     *
     * @param event the event
     */
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public static void onEvent(MouseEvent event)
    {
//        // ensure custom MouseHelper is active
        Minecraft mc = Minecraft.getMinecraft();
       
        if (event.getButton() == 0 && event.isButtonstate())
        {
            EntityPlayer thePlayer = mc.player;
            if (thePlayer != null)
            {
                ItemStack itemstack = thePlayer.getHeldItemMainhand();
                IExtendedReach ieri;
                if (itemstack != null)
                {
                    if (itemstack.getItem() instanceof IExtendedReach)
                    {
                        ieri = (IExtendedReach) itemstack.getItem();
                    }
                    else
                    {
                        ieri = null;
                    }

                    if (ieri != null)
                    {
                        float reach = ieri.getReach();
                        RayTraceResult mov = Utilities.getMouseOverExtended(reach);

                        if (mov != null)
                        {
                            if (mov.entityHit != null && mov.entityHit.hurtResistantTime == 0)
                            {
                                if (mov.entityHit != thePlayer)
                                {
                                    ModNetworking.network.sendToServer(new MessageExtendedReachAttack(mov.entityHit.getEntityId()));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    public static Field parentScreen = ReflectionHelper.findField(GuiCreateWorld.class, "parentScreen", "field_146332_f");

    /**
     * Handling the gui open event to replace the world selection menu with one
     * that defaults to creative mode.
     *
     * @param event the event
     */
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public static void onEvent(GuiOpenEvent event)
    {
        if (event.getGui() instanceof GuiCreateWorld)
        {
            try
            {
                event.setGui(new GuiCreateWorldMod((GuiScreen)(parentScreen.get(event.getGui()))));
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}