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

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

import com.blogspot.jabelarminecraft.examplemod.MainMod;
import com.blogspot.jabelarminecraft.examplemod.blocks.BlockCloud;
import com.blogspot.jabelarminecraft.examplemod.client.MouseHelperAI;
import com.blogspot.jabelarminecraft.examplemod.client.renderers.RenderFactories;
import com.blogspot.jabelarminecraft.examplemod.init.ModKeyBindings;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// TODO: Auto-generated Javadoc
public class ClientProxy implements IProxy
{
    /*
     * For rendering a sphere, need ids for call lists for outside and inside
     */
    public static int sphereIdOutside;
    public static int sphereIdInside;
    
    // mouse helper
    public static MouseHelper mouseHelperAI = new MouseHelperAI(); // used to intercept user mouse movement for "bot" functionality

    /* (non-Javadoc)
     * @see com.blogspot.jabelarminecraft.examplemod.proxy.IProxy#preInit(net.minecraftforge.fml.common.event.FMLPreInitializationEvent)
     */
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        // DEBUG
        System.out.println("on Client side");
        
        Minecraft.getMinecraft().mouseHelper = mouseHelperAI;
        RenderFactories.registerEntityRenderers();
    }

    /* (non-Javadoc)
     * @see com.blogspot.jabelarminecraft.examplemod.proxy.IProxy#init(net.minecraftforge.fml.common.event.FMLInitializationEvent)
     */
    @Override
    public void init(FMLInitializationEvent event)
    {
        // DEBUG
        System.out.println("on Client side");

        // register key bindings
        ModKeyBindings.registerKeyBindings();

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
     * @see com.blogspot.jabelarminecraft.examplemod.proxy.IProxy#shouldSideBeRendered(net.minecraft.block.state.IBlockState, net.minecraft.world.IBlockAccess, net.minecraft.util.math.BlockPos, net.minecraft.util.EnumFacing)
     */
    @SuppressWarnings("deprecation")
    @Override
    public  boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        Minecraft mc = Minecraft.getMinecraft();
        double dist = mc.player.getPositionVector().subtract(new Vec3d(pos)).lengthSquared();
        
        if (dist < 400.0D)
        {
            blockState.withProperty(BlockCloud.TRANSLUCENT, true);
        }
        else
        {
            blockState.withProperty(BlockCloud.TRANSLUCENT, false);
        }
        
        return (dist < 64.0D && Math.abs(pos.getY()-mc.player.getPosition().getY())<4.0D);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void markBlockForUpdate()
    {
        Minecraft mc = Minecraft.getMinecraft();
        
        if (mc.player != null)
        {   
            BlockPos pos = mc.player.getPosition();
            mc.renderGlobal.markBlockRangeForRenderUpdate(pos.getX()-6, pos.getY()-4, pos.getZ()-6, pos.getX()+6, pos.getY()+4, pos.getZ()+6);
        }
    }
}