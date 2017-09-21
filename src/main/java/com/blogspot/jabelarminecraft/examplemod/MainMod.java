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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

// TODO: Auto-generated Javadoc
@Mod( modid = MainMod.MODID, 
      name = MainMod.MODNAME, 
      version = MainMod.MODVERSION,
      guiFactory = "com.blogspot.jabelarminecraft."+MainMod.MODID+".client.gui.GuiFactory",
      acceptedMinecraftVersions = "[1.12]")
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

    // instantiate materials
    // public final static MaterialTanningRack materialTanningRack = new MaterialTanningRack();
    // see custom armor tutorial at: http://bedrockminer.jimdo.com/modding-tutorials/basic-modding/custom-armor/
    // public final static ArmorMaterial SAFEFALLINGLEATHER = EnumHelper.addArmorMaterial("SAFEFALLINGLEATHER", "safe_falling", 5, new int[]{2, 6, 5, 2}, 15);
    
    // blocks are instantiated in BlockRegistry class
    
    // items are instantiated in ItemRegistry class
   
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
    
    // instantiate the mod
    @Instance(MODID)
    public static MainMod instance;
        
    // Says where the client and server 'proxy' code is loaded.
    @SidedProxy(clientSide="com.blogspot.jabelarminecraft.examplemod.proxy.ClientProxy", serverSide="com.blogspot.jabelarminecraft.examplemod.proxy.CommonProxy")
    public static CommonProxy proxy;
    
    // Version checking instance
	public static VersionChecker versionChecker;
	public static boolean haveWarnedVersionOutOfDate = false;
            
    /**
     * Fml life cycle event.
     *
     * @param event the event
     */
    @EventHandler
    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the GameRegistry."
    public void fmlLifeCycleEvent(FMLPreInitializationEvent event) 
    {   	
        // DEBUG
        System.out.println("preInit()"+event.getModMetadata().name);
                
        // hard-code mod information so don't need mcmod.info file
        event.getModMetadata().autogenerated = false ; // stops it from complaining about missing mcmod.info
        event.getModMetadata().credits = TextFormatting.BLUE+MODCREDITS;
        event.getModMetadata().authorList.add(TextFormatting.RED+MODAUTHOR);
        event.getModMetadata().description = TextFormatting.YELLOW+MODDESCRIPTION;
        event.getModMetadata().url = MODURL;
        event.getModMetadata().logoFile = MODLOGO;
        
        proxy.fmlLifeCycleEvent(event);
    }

	/**
	 * Fml life cycle event.
	 *
	 * @param event the event
	 */
	@EventHandler
    // Do your mod setup. Build whatever data structures you care about. Register recipes."
    // Register network handlers
    public void fmlLifeCycleEvent(FMLInitializationEvent event) 
    {
    	
        // DEBUG
        System.out.println("init()");
        
        proxy.fmlLifeCycleEvent(event);
    }

	/**
	 * Fml life cycle.
	 *
	 * @param event the event
	 */
	@EventHandler
    // postInit "Handle interaction with other mods, complete your setup based on this."
    public void fmlLifeCycle(FMLPostInitializationEvent event) 
	{
        // DEBUG
        System.out.println("postInit()");
        
        proxy.fmlLifeCycleEvent(event);
    }

	/**
	 * Fml life cycle.
	 *
	 * @param event the event
	 */
	@EventHandler
	public void fmlLifeCycle(FMLServerAboutToStartEvent event)
	{
        // DEBUG
        System.out.println("Server about to start");
        
		proxy.fmlLifeCycleEvent(event);
	}

	/**
	 * Fml life cycle.
	 *
	 * @param event the event
	 */
	@EventHandler
	// register server commands
	// refer to tutorial at http://www.minecraftforge.net/wiki/Server_Command#Mod_Implementation
	public void fmlLifeCycle(FMLServerStartingEvent event)
	{
        // DEBUG
        System.out.println("Server starting");
        
		proxy.fmlLifeCycleEvent(event);
	}

	/**
	 * Fml life cycle.
	 *
	 * @param event the event
	 */
	@EventHandler
	public void fmlLifeCycle(FMLServerStartedEvent event)
	{
        // DEBUG
        System.out.println("Server started");
        
		proxy.fmlLifeCycleEvent(event);
	}

	/**
	 * Fml life cycle.
	 *
	 * @param event the event
	 */
	@EventHandler
	public void fmlLifeCycle(FMLServerStoppingEvent event)
	{
        // DEBUG
        System.out.println("Server stopping");
        
		proxy.fmlLifeCycleEvent(event);
	}

	/**
	 * Fml life cycle.
	 *
	 * @param event the event
	 */
	@EventHandler
	public void fmlLifeCycle(FMLServerStoppedEvent event)
	{
        // DEBUG
        System.out.println("Server stopped");
        
		proxy.fmlLifeCycleEvent(event);
	}


    /**
     * Save properties.
     */
    public static void saveProperties()
    {
        try
        {
            config.save();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
