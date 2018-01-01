package com.blogspot.jabelarminecraft.examplemod.init;

import com.blogspot.jabelarminecraft.examplemod.MainMod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModCreativeTabs
{
    // instantiate creative tabs
    public static final CustomCreativeTab CREATIVE_TAB = new CustomCreativeTab();

    /**
     * This class is used for an extra tab in the creative inventory. Many
     * mods like to group their special items and blocks in a dedicated tab
     * although it is also perfectly acceptable to put them in the vanilla tabs
     * where it makes sense.
     * */
    public static class CustomCreativeTab extends CreativeTabs
    {
        /**
         * Instantiates a new custom creative tab.
         */
        public CustomCreativeTab()
        {
            // pass a string for the tab label, if you only have one it is common
            // to pass the modid and then in your lang file you can put name of your mod.
            // The unlocalized name of a tab automatically has "itemGroup." prepended.
            super(MainMod.MODID);
        }
    
        @SideOnly(Side.CLIENT)
        @Override
        public ItemStack getTabIconItem()
        {
            return new ItemStack(Items.BANNER);
        }
    
        @SideOnly(Side.CLIENT)
        @Override
        public void displayAllRelevantItems(final NonNullList<ItemStack> items)
        {
            super.displayAllRelevantItems(items);
        }
    }
}