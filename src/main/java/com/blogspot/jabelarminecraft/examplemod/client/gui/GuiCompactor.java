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
import com.blogspot.jabelarminecraft.examplemod.containers.ContainerCompactor;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;;

// TODO: Auto-generated Javadoc
/**
 * @author jabelar
 *
 */
@SideOnly(Side.CLIENT)
public class GuiCompactor extends GuiContainer
{
    private static final ResourceLocation COMPACTOR_GUI_TEXTURES = new ResourceLocation(MainMod.MODID + ":textures/gui/container/compactor.png");
    private final InventoryPlayer inventoryPlayer;
    private final IInventory tileCompactor;

    /**
     * Instantiates a new gui compactor.
     *
     * @param parInventoryPlayer
     *            the par inventory player
     * @param parInventoryCompactor
     *            the par inventory compactor
     */
    public GuiCompactor(InventoryPlayer parInventoryPlayer, IInventory parInventoryCompactor)
    {
        super(new ContainerCompactor(parInventoryPlayer, parInventoryCompactor));
        // DEBUG
        System.out.println("GUI Compactor constructor");
        inventoryPlayer = parInventoryPlayer;
        tileCompactor = parInventoryCompactor;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items). Args : mouseX, mouseY
     *
     * @param mouseX
     *            the mouse X
     * @param mouseY
     *            the mouse Y
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String s = tileCompactor.getDisplayName().getUnformattedText();
        fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        fontRenderer.drawString(inventoryPlayer.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);
    }

    /**
     * Args : renderPartialTicks, mouseX, mouseY.
     *
     * @param partialTicks
     *            the partial ticks
     * @param mouseX
     *            the mouse X
     * @param mouseY
     *            the mouse Y
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(COMPACTOR_GUI_TEXTURES);
        int marginHorizontal = (width - xSize) / 2;
        int marginVertical = (height - ySize) / 2;
        drawTexturedModalRect(marginHorizontal, marginVertical, 0, 0, xSize, ySize);

        // Draw progress indicator
        int progressLevel = getProgressLevel(24);
        drawTexturedModalRect(marginHorizontal + 79, marginVertical + 34, 176, 14, progressLevel + 1, 16);
    }

    private int getProgressLevel(int progressIndicatorPixelWidth)
    {
        int ticksCompactingItemSoFar = tileCompactor.getField(2);
        int ticksPerItem = tileCompactor.getField(3);
        return ticksPerItem != 0 && ticksCompactingItemSoFar != 0 ? ticksCompactingItemSoFar * progressIndicatorPixelWidth / ticksPerItem : 0;
    }

}