/**
    Copyright (C) 2015 by jabelar

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

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.blogspot.jabelarminecraft.blocksmith.BlockSmith;
import com.blogspot.jabelarminecraft.blocksmith.containers.ContainerGrinder;

/**
 * @author jabelar
 *
 */
@SideOnly(Side.CLIENT)
public class GuiGrinder  extends GuiContainer
{
    private static final ResourceLocation grinderGuiTextures = new ResourceLocation(BlockSmith.MODID+":textures/gui/container/grinder.png");
    private final InventoryPlayer inventoryPlayer;
    private final IInventory tileGrinder;

    public GuiGrinder(InventoryPlayer parInventoryPlayer, IInventory parInventoryGrinder)
    {
        super(new ContainerGrinder(parInventoryPlayer, parInventoryGrinder));
        inventoryPlayer = parInventoryPlayer;
        tileGrinder = parInventoryGrinder;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items). Args : mouseX, mouseY
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String s = tileGrinder.getDisplayName().getUnformattedText();
        fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        fontRenderer.drawString(inventoryPlayer.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);
    }

    /**
     * Args : renderPartialTicks, mouseX, mouseY
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(grinderGuiTextures);
        int marginHorizontal = (width - xSize) / 2;
        int marginVertical = (height - ySize) / 2;
        drawTexturedModalRect(marginHorizontal, marginVertical, 0, 0, xSize, ySize);

//        // Draw fuel level indicator
//        if (TileEntityGrinder.func_174903_a(tileGrinder))
//        {
//            int fuelLevel = getFuelLevel(13);
//            drawTexturedModalRect(marginHorizontal + 56, marginVertical + 36 + 12 - fuelLevel, 176, 12 - fuelLevel, 14, fuelLevel + 1);
//        }

        // Draw progress indicator
        int progressLevel = getProgressLevel(24);
        drawTexturedModalRect(marginHorizontal + 79, marginVertical + 34, 176, 14, progressLevel + 1, 16);
    }

    private int getProgressLevel(int progressIndicatorPixelWidth)
    {
        int ticksGrindingItemSoFar = tileGrinder.getField(2); 
        int ticksPerItem = tileGrinder.getField(3);
        return ticksPerItem != 0 && ticksGrindingItemSoFar != 0 ? ticksGrindingItemSoFar * progressIndicatorPixelWidth / ticksPerItem : 0;
    }

//    private int getFuelLevel(int fuelIndicatorPixelHeight)
//    {
//        int currentItemGrindTime = tileGrinder.getField(1); // this is currentItemGrindTime
//
//        if (currentItemGrindTime == 0)
//        {
//            currentItemGrindTime = 200;
//        }
//
//        return tileGrinder.getField(0) * fuelIndicatorPixelHeight / currentItemGrindTime;
//    }
 }