package com.blogspot.jabelarminecraft.examplemod.blocks;

import java.util.Collection;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public abstract class BlockFlowerCloud extends BlockBush
{
    protected PropertyEnum<BlockFlowerCloud.EnumFlowerType> type;

    protected BlockFlowerCloud()
    {
        this.setDefaultState(this.blockState.getBaseState().withProperty(this.getTypeProperty(), this.getBlockType() == BlockFlowerCloud.EnumFlowerColor.RED ? BlockFlowerCloud.EnumFlowerType.POPPY : BlockFlowerCloud.EnumFlowerType.DANDELION));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return super.getBoundingBox(state, source, pos).offset(state.getOffset(source, pos));
    }

    /**
     * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It
     * returns the metadata of the dropped item based on the old metadata of the block.
     */
    @Override
    public int damageDropped(IBlockState state)
    {
        return state.getValue(this.getTypeProperty()).getMeta();
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
    {
        for (BlockFlowerCloud.EnumFlowerType blockflower$enumflowertype : BlockFlowerCloud.EnumFlowerType.getTypes(this.getBlockType()))
        {
            items.add(new ItemStack(this, 1, blockflower$enumflowertype.getMeta()));
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(this.getTypeProperty(), BlockFlowerCloud.EnumFlowerType.getType(this.getBlockType(), meta));
    }

    /**
     * Get the Type of this flower (Yellow/Red)
     */
    public abstract BlockFlowerCloud.EnumFlowerColor getBlockType();

    public IProperty<BlockFlowerCloud.EnumFlowerType> getTypeProperty()
    {
        if (this.type == null)
        {
            this.type = PropertyEnum.<BlockFlowerCloud.EnumFlowerType>create("type", BlockFlowerCloud.EnumFlowerType.class, new Predicate<BlockFlowerCloud.EnumFlowerType>()
            {
                @Override
                public boolean apply(@Nullable BlockFlowerCloud.EnumFlowerType p_apply_1_)
                {
                    return p_apply_1_.getBlockType() == getBlockType();
                }
            });
        }

        return this.type;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(this.getTypeProperty()).getMeta();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {this.getTypeProperty()});
    }

    /**
     * Get the OffsetType for this Block. Determines if the model is rendered slightly offset.
     */
    @Override
    public Block.EnumOffsetType getOffsetType()
    {
        return Block.EnumOffsetType.XZ;
    }

    public static enum EnumFlowerColor
    {
        YELLOW,
        RED;

        public BlockFlower getBlock()
        {
            return this == YELLOW ? Blocks.YELLOW_FLOWER : Blocks.RED_FLOWER;
        }
    }

    public static enum EnumFlowerType implements IStringSerializable
    {
        DANDELION(BlockFlowerCloud.EnumFlowerColor.YELLOW, 0, "dandelion"),
        POPPY(BlockFlowerCloud.EnumFlowerColor.RED, 0, "poppy"),
        BLUE_ORCHID(BlockFlowerCloud.EnumFlowerColor.RED, 1, "blue_orchid", "blueOrchid"),
        ALLIUM(BlockFlowerCloud.EnumFlowerColor.RED, 2, "allium"),
        HOUSTONIA(BlockFlowerCloud.EnumFlowerColor.RED, 3, "houstonia"),
        RED_TULIP(BlockFlowerCloud.EnumFlowerColor.RED, 4, "red_tulip", "tulipRed"),
        ORANGE_TULIP(BlockFlowerCloud.EnumFlowerColor.RED, 5, "orange_tulip", "tulipOrange"),
        WHITE_TULIP(BlockFlowerCloud.EnumFlowerColor.RED, 6, "white_tulip", "tulipWhite"),
        PINK_TULIP(BlockFlowerCloud.EnumFlowerColor.RED, 7, "pink_tulip", "tulipPink"),
        OXEYE_DAISY(BlockFlowerCloud.EnumFlowerColor.RED, 8, "oxeye_daisy", "oxeyeDaisy");

        private static final BlockFlowerCloud.EnumFlowerType[][] TYPES_FOR_BLOCK = new BlockFlowerCloud.EnumFlowerType[BlockFlowerCloud.EnumFlowerColor.values().length][];
        private final BlockFlowerCloud.EnumFlowerColor blockType;
        private final int meta;
        private final String name;
        private final String unlocalizedName;

        private EnumFlowerType(BlockFlowerCloud.EnumFlowerColor blockType, int meta, String name)
        {
            this(blockType, meta, name, name);
        }

        private EnumFlowerType(BlockFlowerCloud.EnumFlowerColor blockType, int meta, String name, String unlocalizedName)
        {
            this.blockType = blockType;
            this.meta = meta;
            this.name = name;
            this.unlocalizedName = unlocalizedName;
        }

        public BlockFlowerCloud.EnumFlowerColor getBlockType()
        {
            return this.blockType;
        }

        public int getMeta()
        {
            return this.meta;
        }

        /**
         * Get the given FlowerType from BlockType & metadata
         */
        public static BlockFlowerCloud.EnumFlowerType getType(BlockFlowerCloud.EnumFlowerColor blockType, int meta)
        {
            BlockFlowerCloud.EnumFlowerType[] ablockflower$enumflowertype = TYPES_FOR_BLOCK[blockType.ordinal()];

            if (meta < 0 || meta >= ablockflower$enumflowertype.length)
            {
                meta = 0;
            }

            return ablockflower$enumflowertype[meta];
        }

        /**
         * Get all FlowerTypes that are applicable for the given Flower block ("yellow", "red")
         */
        public static BlockFlowerCloud.EnumFlowerType[] getTypes(BlockFlowerCloud.EnumFlowerColor flowerColor)
        {
            return TYPES_FOR_BLOCK[flowerColor.ordinal()];
        }

        @Override
        public String toString()
        {
            return this.name;
        }

        @Override
        public String getName()
        {
            return this.name;
        }

        public String getUnlocalizedName()
        {
            return this.unlocalizedName;
        }

        static
        {
            for (final BlockFlowerCloud.EnumFlowerColor blockflower$enumflowercolor : BlockFlowerCloud.EnumFlowerColor.values())
            {
                Collection<BlockFlowerCloud.EnumFlowerType> collection = Collections2.<BlockFlowerCloud.EnumFlowerType>filter(Lists.newArrayList(values()), new Predicate<BlockFlowerCloud.EnumFlowerType>()
                {
                    @Override
                    public boolean apply(@Nullable BlockFlowerCloud.EnumFlowerType p_apply_1_)
                    {
                        return p_apply_1_.getBlockType() == blockflower$enumflowercolor;
                    }
                });
                TYPES_FOR_BLOCK[blockflower$enumflowercolor.ordinal()] = collection.toArray(new BlockFlowerCloud.EnumFlowerType[collection.size()]);
            }
        }
    }
}