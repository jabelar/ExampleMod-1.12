/**
    Copyright (C) 2017 by jabelar

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
package com.blogspot.jabelarminecraft.examplemod.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemSword;

// TODO: Auto-generated Javadoc
/**
 * 
 * @author jabelar
 */
public class ItemSwordExtended extends ItemSword implements IExtendedReach
{

    /**
     * Instantiates a new item sword extended.
     *
     * @param parMaterial the par material
     */
    public ItemSwordExtended(ToolMaterial parMaterial)
    {
        super(parMaterial);
        setCreativeTab(CreativeTabs.COMBAT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.blogspot.jabelarminecraft.examplemod.items.IExtendedReach#getReach()
     */
    @Override
    public float getReach()
    {
        return 30.0F;
    }
}
