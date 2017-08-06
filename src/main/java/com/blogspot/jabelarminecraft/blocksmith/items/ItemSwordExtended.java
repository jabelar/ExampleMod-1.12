/**
 * 
 */
package com.blogspot.jabelarminecraft.blocksmith.items;

import com.blogspot.jabelarminecraft.blocksmith.utilities.Utilities;

import net.minecraft.item.ItemSword;

/**
 * @author jabelar
 *
 */
public class ItemSwordExtended extends ItemSword implements IExtendedReach
{
	public ItemSwordExtended(ToolMaterial parMaterial) 
	{
		super(parMaterial);
		Utilities.setItemName(this, "swordExtended");
	}

	@Override
	public float getReach() 
	{
		return 30.0F;
	}

}
