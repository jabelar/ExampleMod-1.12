package com.blogspot.jabelarminecraft.examplemod.init;

import java.io.File;

import com.blogspot.jabelarminecraft.examplemod.MainMod;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ModConfig
{
    // set up configuration properties (will be read from config file in preInit)
    public static File configFile;
    public static Configuration config;
    public static boolean allowDeconstructUnrealistic = false;
    public static boolean allowDeconstructEnchantedBooks = true;
    public static boolean allowHorseArmorCrafting = true;
    public static boolean allowPartialDeconstructing = true;
    
    /**
     * Process the configuration.
     *
     * @param event
     *            the event
     */
    public static void initConfig(FMLPreInitializationEvent event)
    {
        // might need to use suggestedConfigFile (event.getSuggestedConfigFile) location to publish
        configFile = event.getSuggestedConfigurationFile();
        // DEBUG
        System.out.println(MainMod.MODNAME + " config path = " + configFile.getAbsolutePath());
        System.out.println("Config file exists = " + configFile.canRead());

        config = new Configuration(configFile);

        syncConfig();
    }

    /**
     * Sync config.
     */
    /*
     * sync the configuration want it public so you can handle case of changes made in-game
     */
    public static void syncConfig()
    {
        config.load();
        allowDeconstructUnrealistic = config.get(Configuration.CATEGORY_GENERAL, "All Craftables Can Deconstruct", false,
                "Allow unrealistic deconstruction like pumpkins back from pumpkin seeds").getBoolean(false);
        // DEBUG
        System.out.println("Allow unrealistic deconstruction = " + allowDeconstructUnrealistic);
        allowHorseArmorCrafting = config
                .get(Configuration.CATEGORY_GENERAL, "Can Craft Horse Armor", true, "Allow crafting of horse armor and SADDLEs").getBoolean(true);
        // DEBUG
        System.out.println("Allow horse armor crafting = " + allowHorseArmorCrafting);
        allowDeconstructEnchantedBooks = config
                .get(Configuration.CATEGORY_GENERAL, "Can Deconstruct Enchanted Books", true, "Allow enchanted books to deconstruct like a regular book")
                .getBoolean(true);
        // DEBUG
        System.out.println("Allow enchanted book deconstruction = " + allowDeconstructEnchantedBooks);
        allowPartialDeconstructing = config
                .get(Configuration.CATEGORY_GENERAL, "Allow Partial Deconstruction", true, "Allow deconstruction of stacks that are less than crafting output")
                .getBoolean(true);
        // DEBUG
        System.out.println("Allow partial deconstruction = " + allowPartialDeconstructing);

        // save is useful for the first run where config might not exist, and doesn't hurt
        config.save();
    }
}
