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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.blogspot.jabelarminecraft.examplemod.MainMod;
import com.blogspot.jabelarminecraft.examplemod.client.gui.GuiHandler;
import com.blogspot.jabelarminecraft.examplemod.commands.CommandStructureCapture;
import com.blogspot.jabelarminecraft.examplemod.init.ModFluids;
import com.blogspot.jabelarminecraft.examplemod.networking.MessageExtendedReachAttack;
import com.blogspot.jabelarminecraft.examplemod.networking.MessageRequestItemStackRegistryFromClient;
import com.blogspot.jabelarminecraft.examplemod.networking.MessageSendItemStackRegistryToServer;
import com.blogspot.jabelarminecraft.examplemod.networking.MessageSyncEntityToClient;
import com.blogspot.jabelarminecraft.examplemod.networking.MessageToClient;
import com.blogspot.jabelarminecraft.examplemod.networking.MessageToServer;
import com.blogspot.jabelarminecraft.examplemod.tileentities.TileEntityCompactor;
import com.blogspot.jabelarminecraft.examplemod.worldgen.WorldGenShrine;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

// TODO: Auto-generated Javadoc
public class CommonProxy
{

    protected int modEntityID = 0;

    /*
     * Sometimes useful to have list of all item types, including subtypes
     */
    protected List<ItemStack> itemStackRegistry = new ArrayList<ItemStack>();

    /**
     * Fml life cycle event for Pre-Initialization. Historically (before registry events) this was where blocks, items, etc. were registered. There are still things like entities
     * and networking which should still be registered here.
     *
     * @param event
     *            the event
     */
    public void preInit(FMLPreInitializationEvent event)
    {
        // load configuration before doing anything else
        // got config tutorial from http://www.minecraftforge.net/wiki/How_to_make_an_advanced_configuration_file
        initConfig(event);

        // register stuff
        registerTileEntities();
        registerFluids();
        registerFuelHandlers();
        registerSimpleNetworking();
        // VillagerRegistry.instance().registerVillagerId(10);
        // VillagerRegistry.instance().registerVillageTradeHandler(10, new VillageTradeHandlerMagicBeans());
        // VillagerRegistry.getRegisteredVillagers();
    }

    /**
     * Fml life cycle event for Initialization. This phase is good for registering event listeners, for registering things that depend on things in pre-init from other mods (like
     * recipes, advancements and such.)
     *
     * @param event
     *            the event
     */
    public void init(FMLInitializationEvent event)
    {
        // register Advancements here to allow use of items from other mods
        registerAdvancements();

        // register gui handlers
        registerGuiHandlers();

        // register world generators
        registerWorldGenerators();

        // registerDeconstructingInit(event);
    }

    /**
     * Register gui handlers.
     */
    public void registerGuiHandlers()
    {
        // DEBUG
        System.out.println("Registering gui handlers");
        NetworkRegistry.INSTANCE.registerGuiHandler(MainMod.instance, new GuiHandler());
    }

    /**
     * Register world generators.
     */
    public void registerWorldGenerators()
    {
        // DEBUG
        System.out.println("Registering world generators");
        GameRegistry.registerWorldGenerator(new WorldGenShrine(), 10);
    }

    /**
     * Fml life cycle event Post Initialization. This phase is useful For doing inter-mod stuff like checking which mods are loaded or if you want a complete view of things across
     * mods like having a list of all registered items to aid random item generation.
     *
     * @param event
     *            the event
     */
    public void postInit(FMLPostInitializationEvent event)
    {
        // can do some inter-mod stuff here
        initItemStackRegistry();
    }

    /**
     * Fml life cycle event.
     *
     * @param event
     *            the event
     */
    public void serverStarting(FMLServerStartingEvent event)
    {
        // // register server commands
        event.registerServerCommand(new CommandStructureCapture());
    }

    /*
     * Thanks to diesieben07 tutorial for this code
     */
    /**
     * Registers the simple networking channel and messages for both sides.
     */
    protected void registerSimpleNetworking()
    {
        // DEBUG
        System.out.println("Registering simple networking");
        MainMod.network = NetworkRegistry.INSTANCE.newSimpleChannel(MainMod.NETWORK_CHANNEL_NAME);

        int packetId = 0;
        // register messages from client to server
        MainMod.network.registerMessage(MessageToServer.Handler.class, MessageToServer.class, packetId++, Side.SERVER);
        // register messages from server to client
        MainMod.network.registerMessage(MessageToClient.Handler.class, MessageToClient.class, packetId++, Side.CLIENT);
        MainMod.network.registerMessage(MessageSyncEntityToClient.Handler.class, MessageSyncEntityToClient.class, packetId++, Side.CLIENT);
        MainMod.network.registerMessage(MessageExtendedReachAttack.Handler.class, MessageExtendedReachAttack.class, packetId++, Side.SERVER);
        MainMod.network.registerMessage(MessageSendItemStackRegistryToServer.Handler.class, MessageSendItemStackRegistryToServer.class, packetId++,
                Side.SERVER);
        MainMod.network.registerMessage(MessageRequestItemStackRegistryFromClient.Handler.class, MessageRequestItemStackRegistryFromClient.class, packetId++,
                Side.CLIENT);
    }

    /*
     * Thanks to CoolAlias for this tip!
     */
    /**
     * Returns a side-appropriate EntityPlayer for use during message handling.
     *
     * @param ctx
     *            the ctx
     * @return the player entity from context
     */
    public EntityPlayer getPlayerEntityFromContext(MessageContext ctx)
    {
        return ctx.getServerHandler().player;
    }

    /**
     * Process the configuration.
     *
     * @param event
     *            the event
     */
    protected void initConfig(FMLPreInitializationEvent event)
    {
        // might need to use suggestedConfigFile (event.getSuggestedConfigFile) location to publish
        MainMod.configFile = event.getSuggestedConfigurationFile();
        // DEBUG
        System.out.println(MainMod.MODNAME + " config path = " + MainMod.configFile.getAbsolutePath());
        System.out.println("Config file exists = " + MainMod.configFile.canRead());

        MainMod.config = new Configuration(MainMod.configFile);

        syncConfig();
    }

    /**
     * Sync config.
     */
    /*
     * sync the configuration want it public so you can handle case of changes made in-game
     */
    public void syncConfig()
    {
        MainMod.config.load();
        MainMod.allowDeconstructUnrealistic = MainMod.config.get(Configuration.CATEGORY_GENERAL, "All Craftables Can Deconstruct", false,
                "Allow unrealistic deconstruction like pumpkins back from pumpkin seeds").getBoolean(false);
        // DEBUG
        System.out.println("Allow unrealistic deconstruction = " + MainMod.allowDeconstructUnrealistic);
        MainMod.allowHorseArmorCrafting = MainMod.config
                .get(Configuration.CATEGORY_GENERAL, "Can Craft Horse Armor", true, "Allow crafting of horse armor and SADDLEs").getBoolean(true);
        // DEBUG
        System.out.println("Allow horse armor crafting = " + MainMod.allowHorseArmorCrafting);
        MainMod.allowDeconstructEnchantedBooks = MainMod.config
                .get(Configuration.CATEGORY_GENERAL, "Can Deconstruct Enchanted Books", true, "Allow enchanted books to deconstruct like a regular book")
                .getBoolean(true);
        // DEBUG
        System.out.println("Allow enchanted book deconstruction = " + MainMod.allowDeconstructEnchantedBooks);
        MainMod.allowPartialDeconstructing = MainMod.config
                .get(Configuration.CATEGORY_GENERAL, "Allow Partial Deconstruction", true, "Allow deconstruction of stacks that are less than crafting output")
                .getBoolean(true);
        // DEBUG
        System.out.println("Allow partial deconstruction = " + MainMod.allowPartialDeconstructing);

        // save is useful for the first run where config might not exist, and doesn't hurt
        MainMod.config.save();
    }

    /**
     * 
     * Registers fluids.
     */
    public void registerFluids()
    {
        // DEBUG
        System.out.println("Registering fluids");
        FluidRegistry.registerFluid(ModFluids.SLIME);
        // FluidRegistry.addBucketForFluid(ModFluids.SLIME);
    }

    /**
     * Registers tile entities.
     */
    public void registerTileEntities()
    {
        // DEBUG
        System.out.println("Registering tile entities");
        GameRegistry.registerTileEntity(TileEntityCompactor.class, "tileEntityCompactor");
    }

    /**
     * Register fuel handlers.
     */
    protected void registerFuelHandlers()
    {
        // DEBUG
        System.out.println("Registering fuel handlers");

        // example: GameRegistry.registerFuelHandler(handler);
    }

    /**
     * Register Advancements.
     */
    protected void registerAdvancements()
    {
        // BlockSmith.AdvancementTanningAHide = new Advancement("Advancement.tanningahide", "tanningahide", 0, 0, Items.LEATHER, (Advancement)null);
        // BlockSmith.AdvancementTanningAHide.registerStat().initIndependentStat(); // Eclipse is having trouble chaining these in previous line
        //// BlockSmith.AdvancementGiantSlayer = new Advancement("Advancement.giantslayer", "giantslayer", 2, 1, (Item)null, BlockSmith.AdvancementTanningAHide).setSpecial();
        //// BlockSmith.AdvancementGiantSlayer.registerStat(); // Eclipse is having trouble chaining this in previous line
        // BlockSmith.craftTable = new Advancement("createDecraftTable", "createDecraftTable", 1 - 2 - 2, -1 - 3, BlockSmith.blockDeconstructor, null).registerStat();
        // BlockSmith.deconstructAny = new Advancement("deconstructAnything", "deconstructAnything", 2 - 2, -2 - 2, Items.DIAMOND_HOE, BlockSmith.craftTable).registerStat();
        // BlockSmith.deconstructDiamondHoe = new Advancement("deconstructDiamondHoe", "deconstructDiamondHoe", 2 - 2, 0 - 2, Items.DIAMOND_HOE,
        // BlockSmith.deconstructAny).registerStat();
        // BlockSmith.deconstructJunk = new Advancement("deconstructJunk", "deconstructJunk", 1 - 2, -1 - 2, Items.LEATHER_BOOTS, BlockSmith.deconstructAny).registerStat();
        // BlockSmith.deconstructDiamondShovel = new Advancement("deconstructDiamondShovel", "deconstructDiamondShovel", 3 - 2, -1 - 2, Items.DIAMOND_SHOVEL,
        // BlockSmith.deconstructAny).registerStat();
        // BlockSmith.theHatStandAdvancement = new Advancement("porteManteauAdvancement", "porteManteauAdvancement", 3 - 2, -4 - 2, Blocks.OAK_FENCE,
        // BlockSmith.craftTable).registerStat();
        // AdvancementPage.registerAdvancementPage(new AdvancementPage("BlockSmith",
        // new Advancement[]
        // {
        // BlockSmith.craftTable, BlockSmith.deconstructAny, BlockSmith.deconstructDiamondHoe, BlockSmith.deconstructJunk, BlockSmith.deconstructDiamondShovel,
        // BlockSmith.theHatStandAdvancement
        // }));
        //
        // BlockSmith.deconstructedItemsStat = (StatBasic) (new StatBasic("stat.deconstructeditems", new TextComponentTranslation("stat.deconstructeditems", new
        // Object[0])).registerStat());
        //
    }

    /**
     * Inits the item stack registry.
     */
    protected void initItemStackRegistry()
    {
        return;
    }

    /**
     * Sets the item stack registry.
     *
     * @param parRegistry
     *            the new item stack registry
     */
    public void setItemStackRegistry(List<ItemStack> parRegistry)
    {
        itemStackRegistry = parRegistry;
    }

    /**
     * Gets the item stack registry.
     *
     * @return the item stack registry
     */
    public List<ItemStack> getItemStackRegistry()
    {
        return itemStackRegistry;
    }

    /**
     * Convert item stack list to payload.
     *
     * @param parBuffer
     *            the par buffer
     */
    /*
     * Works directly on passed in ByteBuf to put ItemStack registry into packet payload to be sent to the server
     */
    public void convertItemStackListToPayload(ByteBuf parBuffer)
    {
        Iterator<ItemStack> theIterator = itemStackRegistry.iterator();

        while (theIterator.hasNext())
        {
            ItemStack theStack = theIterator.next();

            // write item id and metadata
            parBuffer.writeInt(Item.getIdFromItem(theStack.getItem()));
            parBuffer.writeInt(theStack.getMetadata());

            // // DEBUG
            // System.out.println(Item.getIdFromItem(theStack.getItem())+" "+theStack.getMetadata());
            boolean hasNBT = theStack.hasTagCompound();
            parBuffer.writeBoolean(hasNBT);
            if (hasNBT)
            {
                // DEBUG
                System.out.println("The stack " + theStack.toString() + " has NBT = " + theStack.getTagCompound().toString());
                ByteBufUtils.writeTag(parBuffer, theStack.getTagCompound());
            }
            theIterator.remove(); // avoids a ConcurrentModificationException
        }

        return;
    }

    /**
     * Convert payload to item stack list.
     *
     * @param theBuffer
     *            the the buffer
     * @return the list
     */
    /*
     * Provides a list of item stacks giving every registered item along with its metadata variants based on a message payload from the client that gives the valid metadata values
     * for those items with variants. Also will include NBT for mods like Tinker's Construct that use NBT on the ItemStacks to make variants instead of metadata.
     */
    public List<ItemStack> convertPayloadToItemStackList(ByteBuf theBuffer)
    {
        List<ItemStack> theList = new ArrayList<ItemStack>();

        while (theBuffer.isReadable())
        {
            int theID = theBuffer.readInt();
            int theMetadata = theBuffer.readInt();
            ItemStack theStack = new ItemStack(Item.getItemById(theID), 1, theMetadata);

            // Handle the case of mods like Tinker's Construct that use NBT instead of metadata
            boolean hasNBT = theBuffer.readBoolean();
            if (hasNBT)
            {
                theStack.setTagCompound(ByteBufUtils.readTag(theBuffer));
                // DEBUG
                System.out.println("The stack " + theStack.toString() + " has NBT = " + theStack.getTagCompound().toString());
            }

            theList.add(theStack);
        }

        // DEBUG
        System.out.println(theList.toString());

        return theList;
    }

    /**
     * handles the acceleration of an object whilst in a material.
     *
     * @param entityIn
     *            the entity in
     * @param materialIn
     *            the material in
     * @return true, if successful
     */
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
}