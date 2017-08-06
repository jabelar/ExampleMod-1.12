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

package com.blogspot.jabelarminecraft.blocksmith.gui;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

/**
 * @author jabelar
 *
 */
public class GuiFactory implements IModGuiFactory 
{
    @Override
    public void initialize(Minecraft minecraftInstance) 
    {
 
    }
 
//    @Override
//    public Class<? extends GuiScreen> mainConfigGuiClass() 
//    {
//        return GuiConfig.class;
//    }
 
    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() 
    {
        return null;
    }
 
//    @Override
//    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) 
//    {
//        return null;
//    }

	@Override
	public boolean hasConfigGui() 
	{
		return false;
	}

	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen) 
	{
		return new GuiConfig(parentScreen);
	}
}