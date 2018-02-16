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

import com.blogspot.jabelarminecraft.examplemod.MainMod;
import com.blogspot.jabelarminecraft.examplemod.init.ModConfig;
import com.blogspot.jabelarminecraft.examplemod.utilities.Utilities;

/**
 * @author jabelar
 *
 */
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiMessageDialog;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.PostConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// TODO: Auto-generated Javadoc
@SideOnly(Side.CLIENT)
public class GuiConfigMod extends GuiConfig
{

    /**
     * Instantiates a new gui config.
     *
     * @param parent
     *            the parent
     */
    public GuiConfigMod(GuiScreen parent)
    {
        super(parent,
                new ConfigElement(ModConfig.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
                MainMod.MODID,
                false,
                false,
                Utilities.stringToGolden("Craft The Way You Want", 7));
        titleLine2 = ModConfig.configFile.getAbsolutePath();
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.minecraftforge.fml.client.config.GuiConfig#drawScreen(int, int, float)
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        title = Utilities.stringToGolden("Craft The Way You Want", 7);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.minecraftforge.fml.client.config.GuiConfig#actionPerformed(net.minecraft.client.gui.GuiButton)
     */
    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 2000)
        {
            // DEBUG
            System.out.println("Pressed DONE button");
            boolean flag = true;
            try
            {
                if ((configID != null || this.parentScreen == null || !(this.parentScreen instanceof GuiConfigMod))
                        && (this.entryList.hasChangedEntry(true)))
                {
                    // DEBUG
                    System.out.println("Saving config elements");
                    boolean requiresMcRestart = this.entryList.saveConfigElements();

                    if (Loader.isModLoaded(modID))
                    {
                        ConfigChangedEvent event = new OnConfigChangedEvent(modID, configID, isWorldRunning, requiresMcRestart);
                        MinecraftForge.EVENT_BUS.post(event);
                        if (!event.getResult().equals(Result.DENY))
                            MinecraftForge.EVENT_BUS.post(new PostConfigChangedEvent(modID, configID, isWorldRunning, requiresMcRestart));

                        if (requiresMcRestart)
                        {
                            flag = false;
                            mc.displayGuiScreen(new GuiMessageDialog(parentScreen, "fml.configgui.gameRestartTitle",
                                    new TextComponentString(I18n.format("fml.configgui.gameRestartRequired")), "fml.configgui.confirmRestartMessage"));
                        }

                        if (this.parentScreen instanceof GuiConfigMod)
                            ((GuiConfigMod) this.parentScreen).needsRefresh = true;
                    }
                }
            }
            catch (Throwable e)
            {
                e.printStackTrace();
            }

            if (flag)
                this.mc.displayGuiScreen(this.parentScreen);
        }
    }

}