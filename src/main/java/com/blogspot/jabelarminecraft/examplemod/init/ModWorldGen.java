package com.blogspot.jabelarminecraft.examplemod.init;

import javax.annotation.Nullable;

import com.blogspot.jabelarminecraft.examplemod.worldgen.WorldGenShrine;
import com.blogspot.jabelarminecraft.examplemod.worldgen.WorldProviderCloud;
import com.blogspot.jabelarminecraft.examplemod.worldgen.WorldTypeCloud;

import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModWorldGen
{  
    public static final String CLOUD_NAME = "cloud";
    public static final DimensionType CLOUD_DIM_TYPE = DimensionType.register(CLOUD_NAME, "_"+CLOUD_NAME, findFreeDimensionID(), WorldProviderCloud.class, true);
    public static final WorldType CLOUD_WORLD_TYPE = new WorldTypeCloud();
    public static final WorldProvider CLOUD_WORLD_PROVIDER = new WorldProviderCloud();
    
    public static final void registerDimensions()
    {
        DimensionManager.registerDimension(CLOUD_DIM_TYPE.getId(), CLOUD_DIM_TYPE);
    }
    
    @Nullable
    private static Integer findFreeDimensionID()
    {
        for (int i=2; i<Integer.MAX_VALUE; i++)
        {
            if (!DimensionManager.isDimensionRegistered(i))
            {
                // DEBUG
                System.out.println("Found free dimension ID = "+i);
                return i;
            }
        }
        
        // DEBUG
        System.out.println("ERROR: Could not find free dimension ID");
        return null;
    }

    /**
     * Register world generators.
     */
    public static void registerWorldGenerators()
    {
        // DEBUG
        System.out.println("Registering world generators");
        GameRegistry.registerWorldGenerator(new WorldGenShrine(), 10);
    }
}
