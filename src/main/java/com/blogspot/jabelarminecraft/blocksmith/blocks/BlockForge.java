/**
 * 
 */
package com.blogspot.jabelarminecraft.blocksmith.blocks;

import java.util.Random;

import com.blogspot.jabelarminecraft.blocksmith.BlockSmith;
import com.blogspot.jabelarminecraft.blocksmith.registries.BlockRegistry;
import com.blogspot.jabelarminecraft.blocksmith.tileentities.TileEntityForge;
import com.blogspot.jabelarminecraft.blocksmith.utilities.Utilities;

import net.minecraft.block.BlockFurnace;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.IScoreCriteria.EnumRenderType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author agilroy
 *
 */
public class BlockForge extends BlockFurnace
{
	// create a property to allow animation of the block model
    public static final PropertyBool FORGE_LIT = PropertyBool.create("forge_lit");

	public BlockForge(boolean parLit) 
	{
		super(parLit);
		if (parLit)
		{
			Utilities.setBlockName(this, "forge_lit");
		}
		else
		{
			Utilities.setBlockName(this, "forge");
			setCreativeTab(CreativeTabs.DECORATIONS);
		}
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
        	System.out.println("onBlockActivated() on server side");
            parPlayer.openGui(BlockSmith.instance, BlockSmith.GUI_ENUM.FORGE.ordinal(), parWorld, parBlockPos.getX(), parBlockPos.getY(), parBlockPos.getZ()); 
        }
        
        return true;
    }

    public static void changeBlockStateContainer(boolean parLit, World parWorld, BlockPos parBlockPos)
    {
    	TileEntity tileentity = parWorld.getTileEntity(parBlockPos);
        parWorld.setBlockState(parBlockPos, BlockRegistry.FORGE.getDefaultState().withProperty(FORGE_LIT, parLit));

        if (tileentity != null)
        {
            tileentity.validate();
            parWorld.setTileEntity(parBlockPos, tileentity);
        }
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    @Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityForge();
    }

//    @Override
//	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
//    {
//        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
//    }
    
    /**
     * Called after the block is set in the Chunk data, but before the Tile Entity is set
     */
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        this.setDefaultFacing(worldIn, pos, state);
    }

    private void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!worldIn.isRemote)
        {
            IBlockState iblockstate = worldIn.getBlockState(pos.north());
            IBlockState iblockstate1 = worldIn.getBlockState(pos.south());
            IBlockState iblockstate2 = worldIn.getBlockState(pos.west());
            IBlockState iblockstate3 = worldIn.getBlockState(pos.east());
            EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);

            if (enumfacing == EnumFacing.NORTH && iblockstate.isFullBlock() && !iblockstate1.isFullBlock())
            {
                enumfacing = EnumFacing.SOUTH;
            }
            else if (enumfacing == EnumFacing.SOUTH && iblockstate1.isFullBlock() && !iblockstate.isFullBlock())
            {
                enumfacing = EnumFacing.NORTH;
            }
            else if (enumfacing == EnumFacing.WEST && iblockstate2.isFullBlock() && !iblockstate3.isFullBlock())
            {
                enumfacing = EnumFacing.EAST;
            }
            else if (enumfacing == EnumFacing.EAST && iblockstate3.isFullBlock() && !iblockstate2.isFullBlock())
            {
                enumfacing = EnumFacing.WEST;
            }

            worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
        }
    }

    @Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);

        if (stack.hasDisplayName())
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityForge)
            {
                ((TileEntityForge)tileentity).setCustomInventoryName(stack.getDisplayName());
            }
        }
    }

    @Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
           TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityForge)
            {
                InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityForge)tileentity);
                worldIn.updateComparatorOutputLevel(pos, this);
            }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
	public boolean hasComparatorInputOverride(IBlockState parBlockState)
    {
        return true;
    }

    @Override
	public int getComparatorInputOverride(IBlockState parBlockState, World worldIn, BlockPos pos)
    {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
	public EnumBlockRenderType getRenderType(IBlockState parBlockState)
    {
        return EnumBlockRenderType.MODEL;
    }

 
    /**
     * Convert the given metadata into a BlockStateContainer for this Block
     */
    @Override
	public IBlockState getStateFromMeta(int meta)
    {
    	IBlockState resultIBlockState = this.getDefaultState();
    	int metaFacing = meta & 7;
    	
        EnumFacing enumfacing = EnumFacing.getFront(metaFacing);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
        {
            enumfacing = EnumFacing.NORTH;
        }

        resultIBlockState = resultIBlockState.withProperty(FACING, enumfacing);
        
        if ((meta & 8) > 0) // check for forge lit
        {
        	resultIBlockState = resultIBlockState.withProperty(FORGE_LIT, true);
        }
        else
        {
        	resultIBlockState = resultIBlockState.withProperty(FORGE_LIT, false);
        }
        
        return resultIBlockState;
    }

    /**
     * Convert the BlockStateContainer into the correct metadata value
     */
    @Override
	public int getMetaFromState(IBlockState state)
    {
    	int resultMeta = state.getValue(FACING).getIndex();
    	if (state.getValue(FORGE_LIT))
    	{
    		resultMeta = resultMeta | 8;
    	}
    	return resultMeta;
    }

    @Override
	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING, FORGE_LIT});
    }

    @Override
	@SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState parIBlockState, World parWorld, BlockPos parBlockPos, Random rand)
    {
        if (parIBlockState.getValue(FORGE_LIT))
        {
        	// DEBUG
        	System.out.println("randomDisplayTick with forge lit = true");
            EnumFacing enumfacing = parIBlockState.getValue(FACING);
            double d0 = parBlockPos.getX() + 0.5D;
            double d1 = parBlockPos.getY() + rand.nextDouble() * 6.0D / 16.0D;
            double d2 = parBlockPos.getZ() + 0.5D;
            double d3 = 0.52D;
            double d4 = rand.nextDouble() * 0.6D - 0.3D;

            switch (BlockForge.SwitchEnumFacing.field_180356_a[enumfacing.ordinal()])
            {
                case 1:
                    parWorld.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
                    parWorld.spawnParticle(EnumParticleTypes.FLAME, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
                    break;
                case 2:
                    parWorld.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
                    parWorld.spawnParticle(EnumParticleTypes.FLAME, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
                    break;
                case 3:
                    parWorld.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D, new int[0]);
                    parWorld.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D, new int[0]);
                    break;
                case 4:
                    parWorld.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D, new int[0]);
                    parWorld.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D, new int[0]);
                    break;
                default:
                    break;
            }
        }
    }
    @SideOnly(Side.CLIENT)
    static final class SwitchEnumFacing
    {
        static final int[] field_180356_a = new int[EnumFacing.values().length];

        static
        {
            try
            {
                field_180356_a[EnumFacing.WEST.ordinal()] = 1;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                field_180356_a[EnumFacing.EAST.ordinal()] = 2;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                field_180356_a[EnumFacing.NORTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                field_180356_a[EnumFacing.SOUTH.ordinal()] = 4;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
