package com.blogspot.jabelarminecraft.examplemod.utilities;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/*
 *  This class holds (one at a time) and returns all the information about an ItemStack
 *  without actually instantiating one. This avoids a lot of unnecessary
 *  performance hit when making an actual copy of an ItemStack as there 
 *  are various methods like initCapabilities which can get called otherwise.
 */

public class ItemStackCopy 
{
	private static int meta;
	private static int count;
	private static NBTTagCompound compound;
	
	public static void store(ItemStack parStack)
	{
		meta = parStack.getItemDamage();
		count = parStack.getCount();
		compound = parStack.getTagCompound();
	}
	
	public static ItemStack restore(ItemStack parStack)
	{
		parStack.setCount(count);
		parStack.setItemDamage(meta);
		parStack.setTagCompound(compound);
		return parStack;
	}
}
