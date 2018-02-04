package com.blogspot.jabelarminecraft.examplemod.worldgen;

import javax.annotation.Nullable;

import com.blogspot.jabelarminecraft.examplemod.init.ModWorldGen;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldProviderCloud extends WorldProvider
{
    private String generatorSettings;
    private float[] colorsSunriseSunset;

    @Override
    public DimensionType getDimensionType()
    {
        return ModWorldGen.CLOUD_DIM_TYPE;
    }

    /**
     * Creates the light to brightness table
     */
    @Override
    protected void generateLightBrightnessTable()
    {
        for (int i = 0; i <= 15; ++i)
        {
            float f1 = 1.0F - i / 15.0F;
            this.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * 1.0F + 0.0F;
        }
    }

    /**
     * Creates a new {@link BiomeProvider} for the WorldProvider, and also sets the values of {@link #hasSkylight} and
     * {@link #hasNoSky} appropriately.
     *  
     * Note that subclasses generally override this method without calling the parent version.
     */
    @Override
    protected void init()
    {
        // DEBUG
        System.out.println("WorldProviderCloud init()");
        
        hasSkyLight = true;
        biomeProvider = getTerrainType().getBiomeProvider(world);
    }

    @Override
    public IChunkGenerator createChunkGenerator()
    {
        return getTerrainType().getChunkGenerator(world, generatorSettings);
    }

    /**
     * Will check if the x, z position specified is alright to be set as the map spawn point
     */
    @Override
    public boolean canCoordinateBeSpawn(int x, int z)
    {
        BlockPos blockpos = new BlockPos(x, 0, z);

        if (world.getBiome(blockpos).ignorePlayerSpawnSuitability())
        {
            return true;
        }
        else
        {
            return world.getGroundAboveSeaLevel(blockpos).getBlock() == Blocks.GRASS;
        }
    }

    /**
     * Calculates the angle of sun and moon in the sky relative to a specified time (usually worldTime)
     */
    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks)
    {
        int i = (int)(worldTime % 24000L);
        float f = (i + partialTicks) / 24000.0F - 0.25F;

        if (f < 0.0F)
        {
            ++f;
        }

        if (f > 1.0F)
        {
            --f;
        }

        float f1 = 1.0F - (float)((Math.cos(f * Math.PI) + 1.0D) / 2.0D);
        f = f + (f1 - f) / 3.0F;
        return f;
    }

    @Override
    public int getMoonPhase(long worldTime)
    {
        return (int)(worldTime / 24000L % 8L + 8L) % 8;
    }

    /**
     * Returns 'true' if in the "main surface world", but 'false' if in the Nether or End dimensions.
     */
    @Override
    public boolean isSurfaceWorld()
    {
        return true;
    }

    /**
     * Returns array with sunrise/sunset colors
     */
    @Override
    @Nullable
    @SideOnly(Side.CLIENT)
    public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks)
    {
        float f1 = MathHelper.cos(celestialAngle * ((float)Math.PI * 2F)) - 0.0F;
        if (f1 >= -0.4F && f1 <= 0.4F)
        {
            float f3 = (f1 - -0.0F) / 0.4F * 0.5F + 0.5F;
            float f4 = 1.0F - (1.0F - MathHelper.sin(f3 * (float)Math.PI)) * 0.99F;
            f4 = f4 * f4;
            this.colorsSunriseSunset[0] = f3 * 0.3F + 0.7F;
            this.colorsSunriseSunset[1] = f3 * f3 * 0.7F + 0.2F;
            this.colorsSunriseSunset[2] = f3 * f3 * 0.0F + 0.2F;
            this.colorsSunriseSunset[3] = f4;
            return this.colorsSunriseSunset;
        }
        else
        {
            return null;
        }
    }

    /**
     * Return Vec3D with biome specific fog color
     */
    @Override
    @SideOnly(Side.CLIENT)
    public Vec3d getFogColor(float p_76562_1_, float p_76562_2_)
    {
        float f = MathHelper.cos(p_76562_1_ * ((float)Math.PI * 2F)) * 2.0F + 0.5F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        float f1 = 0.7529412F;
        float f2 = 0.84705883F;
        float f3 = 1.0F;
        f1 = f1 * (f * 0.94F + 0.06F);
        f2 = f2 * (f * 0.94F + 0.06F);
        f3 = f3 * (f * 0.91F + 0.09F);
        return new Vec3d(f1, f2, f3);
    }

    /**
     * True if the player can respawn in this dimension (true = overworld, false = nether).
     */
    @Override
    public boolean canRespawnHere()
    {
        return true;
    }

    /**
     * the y level at which clouds are rendered.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public float getCloudHeight()
    {
        return this.getTerrainType().getCloudHeight();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isSkyColored()
    {
        return true;
    }

    @Override
    @Nullable
    public BlockPos getSpawnCoordinate()
    {
        return null;
    }

    @Override
    public int getAverageGroundLevel()
    {
        return getTerrainType().getMinimumSpawnHeight(world);
    }

    /**
     * Returns a double value representing the Y value relative to the top of the map at which void fog is at its
     * maximum. The default factor of 0.03125 relative to 256, for example, means the void fog will be at its maximum at
     * (256*0.03125), or 8.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public double getVoidFogYFactor()
    {
        return getTerrainType().voidFadeMagnitude();
    }

    /**
     * Returns true if the given X,Z coordinate should show environmental fog.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public boolean doesXZShowFog(int x, int z)
    {
        return true;
    }

    @Override
    public BiomeProvider getBiomeProvider()
    {
        return biomeProvider;
    }

    @Override
    public boolean doesWaterVaporize()
    {
        return doesWaterVaporize;
    }

    @Override
    public boolean hasSkyLight()
    {
        return hasSkyLight;
    }

    @Override
    public boolean isNether()
    {
        return nether;
    }

    @Override
    public float[] getLightBrightnessTable()
    {
        return lightBrightnessTable;
    }

    @Override
    public WorldBorder createWorldBorder()
    {
        return new WorldBorder();
    }

    /*======================================= Forge Start =========================================*/

    /**
     * The dimension's movement factor.
     * Whenever a player or entity changes dimension from world A to world B, their coordinates are multiplied by
     * worldA.provider.getMovementFactor() / worldB.provider.getMovementFactor()
     * Example: Overworld factor is 1, nether factor is 8. Traveling from overworld to nether multiplies coordinates by 1/8.
     * @return The movement factor
     */
    @Override
    public double getMovementFactor()
    {
        return 1.0;
    }

    /**
     * Allows for manipulating the coloring of the lightmap texture.
     * Will be called for each 16*16 combination of sky/block light values.
     *
     * @param partialTicks Progress between ticks.
     * @param sunBrightness Current sun brightness.
     * @param skyLight Sky light brightness factor.
     * @param blockLight Block light brightness factor.
     * @param colors The color values that will be used: [r, g, b].
     *
     * @see net.minecraft.client.renderer.EntityRenderer#updateLightmap(float)
     */
    @Override
    public void getLightmapColors(float partialTicks, float sunBrightness, float skyLight, float blockLight, float[] colors) {}

    @Override
    public BlockPos getRandomizedSpawnPoint()
    {
        BlockPos ret = this.world.getSpawnPoint();

        boolean isAdventure = world.getWorldInfo().getGameType() == GameType.ADVENTURE;
        int spawnFuzz = this.world instanceof WorldServer ? getTerrainType().getSpawnFuzz((WorldServer)this.world, this.world.getMinecraftServer()) : 1;
        int border = MathHelper.floor(world.getWorldBorder().getClosestDistance(ret.getX(), ret.getZ()));
        if (border < spawnFuzz) spawnFuzz = border;

        if (!isNether() && !isAdventure && spawnFuzz != 0)
        {
            if (spawnFuzz < 2) spawnFuzz = 2;
            int spawnFuzzHalf = spawnFuzz / 2;
            ret = world.getTopSolidOrLiquidBlock(ret.add(spawnFuzzHalf - world.rand.nextInt(spawnFuzz), 0, spawnFuzzHalf - world.rand.nextInt(spawnFuzz)));
        }

        return ret;
    }
    /**
     * Determine if the cursor on the map should 'spin' when rendered, like it does for the player in the nether.
     *
     * @param entity The entity holding the map, playername, or frame-ENTITYID
     * @param x X Position
     * @param z Z Position
     * @param rotation the regular rotation of the marker
     * @return True to 'spin' the cursor
     */
    @Override
    public boolean shouldMapSpin(String entity, double x, double z, double rotation)
    {
        return false;
    }

    /**
     * Determines the dimension the player will be respawned in, typically this brings them back to the overworld.
     *
     * @param player The player that is respawning
     * @return The dimension to respawn the player in
     */
    @Override
    public int getRespawnDimension(EntityPlayerMP player)
    {
        return getDimension();
    }

    /**
     * Called from {@link World#initCapabilities()}, to gather capabilities for this world.
     * It's safe to access world here since this is called after world is registered.
     *
     * On server, called directly after mapStorage and world data such as Scoreboard and VillageCollection are initialized.
     * On client, called when world is constructed, just before world load event is called.
     * Note that this method is always called before the world load event.
     * @return initial holder for capabilities on the world
     */
    @Override
    @Nullable
    public ICapabilityProvider initCapabilities() {
        return null;
    }
    /*======================================= Start Moved From World =========================================*/
    /**
     * The current sun brightness factor for this dimension.
     * 0.0f means no light at all, and 1.0f means maximum sunlight.
     * This will be used for the "calculateSkylightSubtracted"
     * which is for Sky light value calculation.
     *
     * @return The current brightness factor
     * */
    @Override
    public float getSunBrightnessFactor(float par1)
    {
        return world.getSunBrightnessFactor(par1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Vec3d getSkyColor(net.minecraft.entity.Entity cameraEntity, float partialTicks)
    {
        return world.getSkyColorBody(cameraEntity, partialTicks);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Vec3d getCloudColor(float partialTicks)
    {
        return world.getCloudColorBody(partialTicks);
    }

    /**
     * Gets the Sun Brightness for rendering sky.
     * */
    @Override
    @SideOnly(Side.CLIENT)
    public float getSunBrightness(float par1)
    {
        return world.getSunBrightnessBody(par1);
    }

    /**
     * Gets the Star Brightness for rendering sky.
     * */
    @Override
    @SideOnly(Side.CLIENT)
    public float getStarBrightness(float par1)
    {
        return world.getStarBrightnessBody(par1);
    }

    @Override
    public void setAllowedSpawnTypes(boolean allowHostile, boolean allowPeaceful)
    {
        world.setAllowedSpawnTypes(allowHostile, allowPeaceful);
    }

    @Override
    public void calculateInitialWeather()
    {
        world.calculateInitialWeatherBody();
    }

    @Override
    public void updateWeather()
    {
        world.updateWeatherBody();
    }

    @Override
    public boolean canBlockFreeze(BlockPos pos, boolean byWater)
    {
        return false;
    }

    @Override
    public boolean canMineBlock(net.minecraft.entity.player.EntityPlayer player, BlockPos pos)
    {
        return world.canMineBlockBody(player, pos);
    }

    @Override
    public boolean isBlockHighHumidity(BlockPos pos)
    {
        return world.getBiome(pos).isHighHumidity();
    }

    @Override
    public int getHeight()
    {
        return 256;
    }

    @Override
    public int getActualHeight()
    {
        return nether ? 128 : 256;
    }

    @Override
    public double getHorizon()
    {
        return world.getWorldInfo().getTerrainType().getHorizon(world);
    }

    @Override
    public boolean canDoLightning(net.minecraft.world.chunk.Chunk chunk)
    {
        return true;
    }

    @Override
    public boolean canDoRainSnowIce(Chunk chunk)
    {
        return false;
    }
    
    @Override
    public boolean canSnowAt(BlockPos pos, boolean checkLight)
    {
        return false; 
    }

    /**
     * Called when a Player is added to the provider's world.
     */
    @Override
    public void onPlayerAdded(EntityPlayerMP player)
    {
    }

    /**
     * Called when a Player is removed from the provider's world.
     */
    @Override
    public void onPlayerRemoved(EntityPlayerMP player)
    {
    }

    /**
     * Called when the world is performing a save. Only used to save the state of the Dragon Boss fight in
     * WorldProviderEnd in Vanilla.
     */
    @Override
    public void onWorldSave()
    {
    }

    /**
     * Called when the world is updating entities. Only used in WorldProviderEnd to update the DragonFightManager in
     * Vanilla.
     */
    @Override
    public void onWorldUpdateEntities()
    {
    }

    /**
     * Called to determine if the chunk at the given chunk coordinates within the provider's world can be dropped. Used
     * in WorldProviderSurface to prevent spawn chunks from being unloaded.
     */
    @Override
    public boolean canDropChunk(int x, int z)
    {
        return !this.world.isSpawnChunk(x, z) || !this.world.provider.getDimensionType().shouldLoadSpawn();
    }

    public WorldType getTerrainType()
    {
        return ModWorldGen.CLOUD_WORLD_TYPE;
    }
}
