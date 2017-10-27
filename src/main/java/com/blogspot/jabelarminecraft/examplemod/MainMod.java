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

package com.blogspot.jabelarminecraft.examplemod;

import java.io.File;

import com.blogspot.jabelarminecraft.examplemod.creativetabs.CustomCreativeTab;
import com.blogspot.jabelarminecraft.examplemod.proxy.CommonProxy;

import net.minecraft.advancements.Advancement;
import net.minecraft.stats.StatBasic;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

/**
 * This is the main file for the mod, as it has the mod annotation
 */
@Mod(   modid = MainMod.MODID,
        name = MainMod.MODNAME,
        version = MainMod.MODVERSION,
        guiFactory = "com.blogspot.jabelarminecraft." + MainMod.MODID + ".client.gui.GuiFactory",
        acceptedMinecraftVersions = "[1.12]",
        updateJSON = "https://raw.githubusercontent.com/jabelar/ExampleMod-1.12/master/src/main/resources/versionChecker.json"
        )
public class MainMod
{
    public static final String MODID = "examplemod";
    public static final String MODNAME = "Example Mod";
    public static final String MODVERSION = "1.0.0";
    public static final String MODDESCRIPTION = "Describe your mod here";
    public static final String MODAUTHOR = "jabelar";
    public static final String MODCREDITS = "Jnaejnae";
    public static final String MODURL = "www.jabelarminecraft.blogspot.com";
    public static final String MODLOGO = "modconfigraphic.png";

    // use a named channel to identify packets related to this mod
    public static final String NETWORK_CHANNEL_NAME = MODID;
    public static FMLEventChannel channel;

    // networking
    public static SimpleNetworkWrapper network;

    // set up configuration properties (will be read from config file in preInit)
    public static File configFile;
    public static Configuration config;
    public static boolean allowDeconstructUnrealistic = false;
    public static boolean allowDeconstructEnchantedBooks = true;
    public static boolean allowHorseArmorCrafting = true;
    public static boolean allowPartialDeconstructing = true;

    // instantiate creative tabs
    public static final CustomCreativeTab CREATIVE_TAB = new CustomCreativeTab();

    // materials are instantiated in the ModMaterials class
    
    // blocks are instantiated in ModBlocks class

    // items are instantiated in ModItems class

    // instantiate structures
    // important to do this after blocks in case structure uses custom block

    // instantiate advancements and stats
    public static Advancement advancementUseCompactor;
    public static StatBasic deconstructedItemsStat;

    // enumerate guis
    public enum GUI_ENUM
    {
        COMPACTOR
    }

    static
    {
        FluidRegistry.enableUniversalBucket();
    }

    // instantiate the mod
    @Instance(MODID)
    public static MainMod instance;

    // Says where the client and server 'proxy' code is loaded.
    @SidedProxy(clientSide = "com.blogspot.jabelarminecraft.examplemod.proxy.ClientProxy",
            serverSide = "com.blogspot.jabelarminecraft.examplemod.proxy.CommonProxy")
    public static CommonProxy proxy;

    // Version checking instance
    public static VersionChecker versionChecker;
    public static boolean haveWarnedVersionOutOfDate = false;

    /**
     * Pre-Initialization FML Life Cycle event handling method which is automatically
     * called by Forge. It must be annotated as an event handler.
     *
     * @param event
     *            the event
     */
    @EventHandler
    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the GameRegistry."
    public void preInit(FMLPreInitializationEvent event)
    {
        // DEBUG
        System.out.println("preInit()" + event.getModMetadata().name);

        // hard-code mod information so don't need mcmod.info file
        event.getModMetadata().autogenerated = false; // stops it from complaining about missing mcmod.info
        event.getModMetadata().credits = TextFormatting.BLUE + MODCREDITS;
        event.getModMetadata().authorList.add(TextFormatting.RED + MODAUTHOR);
        event.getModMetadata().description = TextFormatting.YELLOW + MODDESCRIPTION;
        event.getModMetadata().url = MODURL;
        event.getModMetadata().logoFile = MODLOGO;

        proxy.preInit(event);
    }

    /**
     * Initialization FML Life Cycle event handling method which is automatically
     * called by Forge. It must be annotated as an event handler.
     *
     * @param event
     *            the event
     */
    @EventHandler
    // Do your mod setup. Build whatever data structures you care about. Register recipes."
    // Register network handlers
    public void init(FMLInitializationEvent event)
    {

        // DEBUG
        System.out.println("init()");

        proxy.init(event);
    }

    /**
     * Post-Initialization FML Life Cycle event handling method which is automatically
     * called by Forge. It must be annotated as an event handler.
     *
     * @param event
     *            the event
     */
    @EventHandler
    // postInit "Handle interaction with other mods, complete your setup based on this."
    public void postInit(FMLPostInitializationEvent event)
    {
        // DEBUG
        System.out.println("postInit()");

        proxy.postInit(event);
    }


    /**
     * Fml life cycle.
     *
     * @param event
     *            the event
     */
    @EventHandler
    // register server commands
    // refer to tutorial at http://www.minecraftforge.net/wiki/Server_Command#Mod_Implementation
    public void serverStarting(FMLServerStartingEvent event)
    {
        // DEBUG
        System.out.println("Server starting");

        proxy.serverStarting(event);
    }
}
