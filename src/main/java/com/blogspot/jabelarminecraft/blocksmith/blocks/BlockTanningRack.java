/**
    Copyright (C) 2014 by jabelar

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

package com.blogspot.jabelarminecraft.blocksmith.blocks;

import java.util.Random;

import com.blogspot.jabelarminecraft.blocksmith.BlockSmith;
import com.blogspot.jabelarminecraft.blocksmith.registries.BlockRegistry;
import com.blogspot.jabelarminecraft.blocksmith.tileentities.TileEntityTanningRack;
import com.blogspot.jabelarminecraft.blocksmith.utilities.Utilities;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author jabelar
 *
 */
public class BlockTanningRack extends BlockContainer
{
	// create a property to allow animation of the block model
    public static final PropertyInteger TANNING_INGREDIENT = PropertyInteger.create("tanning_ingredient", 0, 6);
    private boolean hasTileEntity;

    public BlockTanningRack()
    {
        super(BlockSmith.materialTanningRack);
        // DEBUG
        System.out.println("BlockTanningRack constructor");
        setDefaultState(blockState.getBaseState().withProperty(TANNING_INGREDIENT, 0));
        // override default values of Block, where appropriate
        Utilities.setBlockName(this, "tanningrack");
        setCreativeTab(CreativeTabs.DECORATIONS);
        blockSoundType = SoundType.SNOW;
        blockParticleGravity = 1.0F;
        slipperiness = 0.6F;
        lightOpacity = 20; // cast a light shadow
        setTickRandomly(false);
        useNeighborBrightness = false;
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState parIBlockState, IBlockAccess parWorld, BlockPos parBlockPos)
    {
        // bounding box is 1.5 blocks high to match model
        return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.5F);
    }

    public static void changeBlockBasedOnTanningStatus(int parTanningIngredient, World parWorld, BlockPos parBlockPos)
    {
        TileEntity theTileEntity = parWorld.getTileEntity(parBlockPos);

//    	// DEBUG
//    	System.out.println("changeBlockBasedOnTanningStatus() with tanning complete = "+parTanningComplete);
        parWorld.setBlockState(parBlockPos, BlockRegistry.TANNING_RACK.getDefaultState().withProperty(TANNING_INGREDIENT, parTanningIngredient), 3);
        if (theTileEntity != null)
        {
            theTileEntity.validate();
            parWorld.setTileEntity(parBlockPos, theTileEntity);
        }
    }
    
    /**
     * Get the Item that this Block should drop when harvested.
     *  
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    @Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(BlockRegistry.TANNING_RACK);
    }

    @Override
	public void onBlockAdded(World parWorld, BlockPos parBlockPos, IBlockState parIBlockState)
    {
        if (!parWorld.isRemote)
        {
            parWorld.setBlockState(parBlockPos, parIBlockState.withProperty(TANNING_INGREDIENT, 0), 2);
        }
    }

    @Override
	@SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World worldIn, BlockPos pos, Random rand)
    {
        // can create a particle effect here
    }

    @Override
	public boolean onBlockActivated(
			World parWorld, 
			BlockPos parBlockPos, 
			IBlockState parIBlockState, 
			EntityPlayer parPlayer, 
			EnumHand parHand, 
			EnumFacing side, 
			float hitX, 
			float hitY, 
			float hitZ
			)
    {
        if (!parWorld.isRemote)
        {
        	// DEBUG
        	System.out.println("BlockTanningRack onBlockActivated() on server side");
            parPlayer.openGui(BlockSmith.instance, BlockSmith.GUI_ENUM.TANNING_RACK.ordinal(), parWorld, parBlockPos.getX(), parBlockPos.getY(), parBlockPos.getZ()); 
        }
        
        return true;
    }


    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    @Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
    {
    	// DEBUG
    	System.out.println("BlockTanningRack createNewTileEntity()");
        return new TileEntityTanningRack();
    }

//    @Override
//	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
//    {
//        return getDefaultState().withProperty(TANNING_INGREDIENT, 0);
//    }

    @Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {

        if (stack.hasDisplayName())
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityTanningRack)
            {
                ((TileEntityTanningRack)tileentity).setCustomInventoryName(stack.getDisplayName());
            }
        }
    }

    @Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!hasTileEntity)
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityTanningRack)
            {
                InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityTanningRack)tileentity);
                worldIn.updateComparatorOutputLevel(pos, this);
            }
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
	public boolean hasComparatorInputOverride(IBlockState parIBlockState)
    {
        return true;
    }

    @Override
	public int getComparatorInputOverride(IBlockState parIBlockState, World worldIn, BlockPos pos)
    {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }

    @Override
	@SideOnly(Side.CLIENT)
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState parIBlockState)
    {
        return new ItemStack(Item.getItemFromBlock(BlockRegistry.TANNING_RACK));
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
	public EnumBlockRenderType getRenderType(IBlockState parIBlockState)
    {
        return EnumBlockRenderType.MODEL;
    }
    

    /**
     * Convert the given metadata into a BlockStateContainer for this Block
     */
    @Override
	public IBlockState getStateFromMeta(int meta)
    {
    	return getDefaultState().withProperty(TANNING_INGREDIENT, meta);
    }

    /**
     * Convert the BlockStateContainer into the correct metadata value
     */
    @Override
	public int getMetaFromState(IBlockState state)
    {
    	return state.getValue(TANNING_INGREDIENT);
    }

    @Override
	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {TANNING_INGREDIENT});
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
    	return BlockRenderLayer.TRANSLUCENT;
    }
    
    @Override
	public boolean isOpaqueCube(IBlockState parIBlockState)
    {
    	return parIBlockState.getMaterial().isOpaque();
    }
}