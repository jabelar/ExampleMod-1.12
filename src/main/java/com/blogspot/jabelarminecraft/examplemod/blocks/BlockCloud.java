package com.blogspot.jabelarminecraft.examplemod.blocks;

import com.blogspot.jabelarminecraft.examplemod.init.ModMaterials;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author jabelar
 *
 */
public class BlockCloud extends Block
{

    public BlockCloud()
    {
        super(ModMaterials.CLOUD);
        
        // DEBUG
        System.out.println("BlockCloud constructor");
        
        // override default values of Block, where appropriate
//        setUnlocalizedName("magicbeanscloud");
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        blockSoundType = SoundType.SNOW;
        blockParticleGravity = 1.0F;
        slipperiness = 0.6F;
        lightOpacity = 20; // cast a light shadow
        setBlockUnbreakable();
        setTickRandomly(false);
        setLightLevel(0.5F); // redstone light has light value of 1.0F
        useNeighborBrightness = false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }
//    
//    @Override
//    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
//    {
//        return false;
//    }
//    
////    /**
////     * Returns true if the given side of this block type should be rendered (if it's solid or not), if the adjacent
////     * block is at the given coordinates. Args: blockAccess, x, y, z, side
////     */
////    @Override
////    public boolean isBlockSolid(IBlockAccess p_149747_1_, BlockPos parPos, EnumFacing parSide)
////    {
////        return getMaterial(getDefaultState()).isSolid();
////    }
//
//    /**
//     * Used to determine ambient occlusion and culling when rebuilding chunks for render
//     */
//    @Override
//    public boolean isOpaqueCube(IBlockState state)
//    {
//        return false;
//    }
//
//    /**
//     * Checks if the block is a solid face on the given side, used by placement logic.
//     *
//     * @param world The current world
//     * @param x X Position
//     * @param y Y position
//     * @param z Z position
//     * @param side The side to check
//     * @return True if the block is solid on the specified side.
//     */
//    @Override
//    public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos parPos, EnumFacing parSide)
//    {
//        return true; 
//    }
//    
//    @Override
//    public boolean isNormalCube(IBlockState state)
//    {
//        return true;
//    }
//    
//    @Override
//    public boolean isNormalCube(IBlockState parState, IBlockAccess parWorld, BlockPos parPos)
//    {
//        return true;
//    }
//    
////    @Override
////    @SideOnly(Side.CLIENT)
////    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
////    {
////        return true;
////    }
//
//    /**
//     * Determines if the current block is replaceable by Ore veins during world generation.
//     *
//     * @param state The current state
//     * @param world The current world
//     * @param pos Block position in world
//     * @param target The generic target block the gen is looking for, Standards define stone
//     *      for overworld generation, and neatherack for the nether.
//     * @return True to allow this block to be replaced by a ore
//     */
//    @Override
//    public boolean isReplaceableOreGen(IBlockState state, IBlockAccess world, BlockPos pos, com.google.common.base.Predicate<IBlockState> target)
//    {
//        return target.apply(state);
//    }
//
//    /**
//     * Chance that fire will spread and consume this block.
//     * 300 being a 100% chance, 0, being a 0% chance.
//     *
//     * @param world The current world
//     * @param x The blocks X position
//     * @param y The blocks Y position
//     * @param z The blocks Z position
//     * @param face The face that the fire is coming from
//     * @return A number ranging from 0 to 300 relating used to determine if the block will be consumed by fire
//     */
//    @Override
//    public int getFlammability(IBlockAccess world, BlockPos parPos, EnumFacing parSide)
//    {
//        return 0;
//    }
//
//    /**
//     * Currently only called by fire when it is on top of this block.
//     * Returning true will prevent the fire from naturally dying during updating.
//     * Also prevents firing from dying from rain.
//     *
//     * @param world The current world
//     * @param x The blocks X position
//     * @param y The blocks Y position
//     * @param z The blocks Z position
//     * @param metadata The blocks current metadata
//     * @param side The face that the fire is coming from
//     * @return True if this block sustains fire, meaning it will never go out.
//     */
//    @Override
//    public boolean isFireSource(World parWorld, BlockPos parPos, EnumFacing parSide)
//    {
//        return false;
//    }
//
//    /**
//     * Metadata and fortune sensitive version, this replaces the old (int meta, Random rand)
//     * version in 1.1.
//     *
//     * @param meta Blocks Metadata
//     * @param fortune Current item fortune level
//     * @param random Random number generator
//     * @return The number of items to drop
//     */
//    @Override
//    public int quantityDropped(IBlockState parState, int parFortune, Random parRandom)
//    {
//        /**
//         * Returns the usual quantity dropped by the block plus a bonus of 1 to 'i' (inclusive).
//         */
//        return 0;
//    }
//
//    /**
//     * This returns a complete list of items dropped from this block.
//     *
//     * @param world The current world
//     * @param x X Position
//     * @param y Y Position
//     * @param z Z Position
//     * @param metadata Current metadata
//     * @param fortune Breakers fortune level
//     * @return A ArrayList containing all items this block drops
//     */
//    @Override
//    public ArrayList<ItemStack> getDrops(IBlockAccess world, BlockPos parPos, IBlockState parState, int parFortune)
//    {
//        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
//        return ret;
//    }
//
//    @Override
//    public boolean canSilkHarvest(World parWorld, BlockPos parPos, IBlockState parState, EntityPlayer parPlayer)
//    {
//        return false;
//    }
//
//    @Override
//    public boolean canCreatureSpawn(IBlockState state, IBlockAccess parWorld, BlockPos parPos, SpawnPlacementType parType)
//    {
//        // TODO
//        // probably want to limit by creature type
//        return true;
//    }
//
//    @Override
//    public boolean shouldCheckWeakPower(IBlockState state, IBlockAccess parWorld, BlockPos parPos, EnumFacing parSide)
//    {
//        return false;
//    }
//
//    /**
//     * Checks if the specified tool type is efficient on this block, 
//     * meaning that it digs at full speed.
//     * 
//     * @param type
//     * @param metadata
//     * @return
//     */
//    @Override
//    public boolean isToolEffective(String parType, IBlockState parState)
//    {
//        return false;
//    }
}
