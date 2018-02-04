package com.blogspot.jabelarminecraft.examplemod.materials;


import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

/**
 * @author jabelar
 *
 */
public class MaterialCloud extends Material
{
    public MaterialCloud() 
    {
        super(MapColor.SNOW);
        setAdventureModeExempt();
    }

//    /**
//     * Returns if blocks of these materials are liquids.
//     */
//    @Override
//    public boolean isLiquid()
//    {
//        return false;
//    }
//
//    @Override
//    public boolean isSolid()
//    {
//        return false;
//    }
//    
//    /**
//     * Returns if this material is considered solid or not
//     */
//    @Override
//    public boolean blocksMovement()
//    {
//        return true;
//    }
//
//    /**
//     * Returns if the block can burn or not.
//     */
//    @Override
//    public boolean getCanBurn()
//    {
//        return false;
//    }
//
//    /**
//     * Returns whether the material can be replaced by other blocks when placed - eg snow, vines and tall grass.
//     */
//    @Override
//    public boolean isReplaceable()
//    {
//        return false;
//    }

    /**
     * Indicate if the material is opaque
     */
    @Override
    public boolean isOpaque()
    {
        return false;
    }
//
//    /**
//     * Returns true if the material can be harvested without a tool (or with the wrong tool)
//     */
//    @Override
//    public boolean isToolNotRequired()
//    {
//        return false;
//    }
//
//    /**
//     * Returns the mobility information of the material, 0 = free, 1 = can't push but can move over, 2 = total
//     * immobility and stop pistons.
//     */
//    @Override
//    public EnumPushReaction getMobilityFlag()
//    {
//        return EnumPushReaction.BLOCK;
//    }
}
