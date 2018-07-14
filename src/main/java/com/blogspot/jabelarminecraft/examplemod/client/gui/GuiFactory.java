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

package com.blogspot.jabelarminecraft.examplemod.client.gui;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// TODO: Auto-generated Javadoc
/**
 * @author jabelar
 *
 */
@SideOnly(Side.CLIENT)
public class GuiFactory implements IModGuiFactory
{

    /* (non-Javadoc)
     * @see net.minecraftforge.fml.client.IModGuiFactory#initialize(net.minecraft.client.Minecraft)
     */
    @Override
    public void initialize(Minecraft minecraftInstance)
    {

    }

    /* (non-Javadoc)
     * @see net.minecraftforge.fml.client.IModGuiFactory#runtimeGuiCategories()
     */
    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
    {
        return null;
    }

    /* (non-Javadoc)
     * @see net.minecraftforge.fml.client.IModGuiFactory#hasConfigGui()
     */
    @Override
    public boolean hasConfigGui()
    {
        return true;
    }


    /* (non-Javadoc)
     * @see net.minecraftforge.fml.client.IModGuiFactory#createConfigGui(net.minecraft.client.gui.GuiScreen)
     */
    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen)
    {
        return new GuiConfigMod(parentScreen);
    }
}