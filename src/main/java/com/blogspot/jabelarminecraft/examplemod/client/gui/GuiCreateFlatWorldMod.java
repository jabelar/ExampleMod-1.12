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

import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.FlatLayerInfo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// TODO: Auto-generated Javadoc
@SideOnly(Side.CLIENT)
public class GuiCreateFlatWorldMod extends GuiScreen
{
    private final GuiCreateWorldMod createWorldGui;
    private FlatGeneratorInfo generatorInfo = FlatGeneratorInfo.getDefaultFlatGenerator();
    /** The title given to the flat world currently in creation */
    private String flatWorldTitle;
    /** The text used to identify the material for a layer */
    private String materialText;
    /** The text used to identify the height of a layer */
    private String heightText;
    private Details createFlatWorldListSlotGui;
    /** The (unused and permenantly hidden) add layer button */
    private GuiButton addLayerButton;
    /** The (unused and permenantly hidden) edit layer button */
    private GuiButton editLayerButton;
    /** The remove layer button */
    private GuiButton removeLayerButton;

    /**
     * Instantiates a new gui create flat world mod.
     *
     * @param createWorldGuiIn the create world gui in
     * @param preset the preset
     */
    public GuiCreateFlatWorldMod(GuiCreateWorldMod createWorldGuiIn, String preset)
    {
        this.createWorldGui = createWorldGuiIn;
        this.setPreset(preset);
    }

    /**
     * Gets the superflat preset in the text format described on the Superflat article on the Minecraft Wiki.
     *
     * @return the preset
     */
    public String getPreset()
    {
        return this.generatorInfo.toString();
    }

    /**
     * Sets the superflat preset. Invalid or null values will result in the default superflat preset being used.
     *
     * @param preset the new preset
     */
    public void setPreset(String preset)
    {
        this.generatorInfo = FlatGeneratorInfo.createFlatGeneratorFromString(preset);
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    @Override
    public void initGui()
    {
        this.buttonList.clear();
        this.flatWorldTitle = I18n.format("createWorld.customize.flat.title");
        this.materialText = I18n.format("createWorld.customize.flat.tile");
        this.heightText = I18n.format("createWorld.customize.flat.height");
        this.createFlatWorldListSlotGui = new Details();
        this.addLayerButton = this.addButton(new GuiButton(2, this.width / 2 - 154, this.height - 52, 100, 20, I18n.format("createWorld.customize.flat.addLayer") + " (NYI)"));
        this.editLayerButton = this.addButton(new GuiButton(3, this.width / 2 - 50, this.height - 52, 100, 20, I18n.format("createWorld.customize.flat.editLayer") + " (NYI)"));
        this.removeLayerButton = this.addButton(new GuiButton(4, this.width / 2 - 155, this.height - 52, 150, 20, I18n.format("createWorld.customize.flat.removeLayer")));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 155, this.height - 28, 150, 20, I18n.format("gui.done")));
        this.buttonList.add(new GuiButton(5, this.width / 2 + 5, this.height - 52, 150, 20, I18n.format("createWorld.customize.presets")));
        this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height - 28, 150, 20, I18n.format("gui.cancel")));
        this.addLayerButton.visible = false;
        this.editLayerButton.visible = false;
        this.generatorInfo.updateLayers();
        this.onLayersChanged();
    }

    /**
     * Handles mouse input.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Override
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
        this.createFlatWorldListSlotGui.handleMouseInput();
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     *
     * @param button the button
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        int i = this.generatorInfo.getFlatLayers().size() - this.createFlatWorldListSlotGui.selectedLayer - 1;

        if (button.id == 1)
        {
            this.mc.displayGuiScreen(this.createWorldGui);
        }
        else if (button.id == 0)
        {
            this.createWorldGui.chunkProviderSettingsJson = this.getPreset();
            this.mc.displayGuiScreen(this.createWorldGui);
        }
        else if (button.id == 5)
        {
            this.mc.displayGuiScreen(new GuiFlatPresetsMod(this));
        }
        else if (button.id == 4 && this.hasSelectedLayer())
        {
            this.generatorInfo.getFlatLayers().remove(i);
            this.createFlatWorldListSlotGui.selectedLayer = Math.min(this.createFlatWorldListSlotGui.selectedLayer, this.generatorInfo.getFlatLayers().size() - 1);
        }

        this.generatorInfo.updateLayers();
        this.onLayersChanged();
    }

    /**
     * Would update whether or not the edit and remove buttons are enabled, but is currently disabled and always
     * disables the buttons (which are invisible anyways).
     */
    public void onLayersChanged()
    {
        boolean flag = this.hasSelectedLayer();
        this.removeLayerButton.enabled = flag;
        this.editLayerButton.enabled = flag;
        this.editLayerButton.enabled = false;
        this.addLayerButton.enabled = false;
    }

    /**
     * Returns whether there is a valid layer selection
     */
    private boolean hasSelectedLayer()
    {
        return this.createFlatWorldListSlotGui.selectedLayer > -1 && this.createFlatWorldListSlotGui.selectedLayer < this.generatorInfo.getFlatLayers().size();
    }

    /**
     * Draws the screen and all the components in it.
     *
     * @param mouseX the mouse X
     * @param mouseY the mouse Y
     * @param partialTicks the partial ticks
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.createFlatWorldListSlotGui.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRenderer, this.flatWorldTitle, this.width / 2, 8, 16777215);
        int i = this.width / 2 - 92 - 16;
        this.drawString(this.fontRenderer, this.materialText, i, 32, 16777215);
        this.drawString(this.fontRenderer, this.heightText, i + 2 + 213 - this.fontRenderer.getStringWidth(this.heightText), 32, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @SideOnly(Side.CLIENT)
    class Details extends GuiSlot
    {
        /**
         * The currently selected layer; -1 if there is no selection. This is in the order that it is displayed on-
         * screen, with the topmost layer having index 0.
         */
        public int selectedLayer = -1;

        /**
         * Instantiates a new details.
         */
        public Details()
        {
            super(GuiCreateFlatWorldMod.this.mc, GuiCreateFlatWorldMod.this.width, GuiCreateFlatWorldMod.this.height, 43, GuiCreateFlatWorldMod.this.height - 60, 24);
        }

        /**
         * Draws an item with a background at the given coordinates. The item and its background are 20 pixels tall/wide
         * (though only the inner 18x18 is actually drawn on)
         */
        private void drawItem(int x, int z, ItemStack itemToDraw)
        {
            this.drawItemBackground(x + 1, z + 1);
            GlStateManager.enableRescaleNormal();

            if (!itemToDraw.isEmpty())
            {
                RenderHelper.enableGUIStandardItemLighting();
                GuiCreateFlatWorldMod.this.itemRender.renderItemIntoGUI(itemToDraw, x + 2, z + 2);
                RenderHelper.disableStandardItemLighting();
            }

            GlStateManager.disableRescaleNormal();
        }

        /**
         * Draws the background icon for an item, with the indented texture from stats.png
         */
        private void drawItemBackground(int x, int y)
        {
            this.drawItemBackground(x, y, 0, 0);
        }

        /**
         * Draws the background icon for an item, using a texture from stats.png with the given coords
         */
        private void drawItemBackground(int x, int z, int textureX, int textureY)
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.getTextureManager().bindTexture(Gui.STAT_ICONS);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(x + 0, z + 18, GuiCreateFlatWorldMod.this.zLevel).tex((textureX + 0) * 0.0078125F, (textureY + 18) * 0.0078125F).endVertex();
            bufferbuilder.pos(x + 18, z + 18, GuiCreateFlatWorldMod.this.zLevel).tex((textureX + 18) * 0.0078125F, (textureY + 18) * 0.0078125F).endVertex();
            bufferbuilder.pos(x + 18, z + 0, GuiCreateFlatWorldMod.this.zLevel).tex((textureX + 18) * 0.0078125F, (textureY + 0) * 0.0078125F).endVertex();
            bufferbuilder.pos(x + 0, z + 0, GuiCreateFlatWorldMod.this.zLevel).tex((textureX + 0) * 0.0078125F, (textureY + 0) * 0.0078125F).endVertex();
            tessellator.draw();
        }

        /* (non-Javadoc)
         * @see net.minecraft.client.gui.GuiSlot#getSize()
         */
        @Override
        protected int getSize()
        {
            return GuiCreateFlatWorldMod.this.generatorInfo.getFlatLayers().size();
        }

        /**
         * The element in the slot that was clicked, boolean for whether it was double clicked or not.
         *
         * @param slotIndex the slot index
         * @param isDoubleClick the is double click
         * @param mouseX the mouse X
         * @param mouseY the mouse Y
         */
        @Override
        protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY)
        {
            this.selectedLayer = slotIndex;
            GuiCreateFlatWorldMod.this.onLayersChanged();
        }

        /**
         * Returns true if the element passed in is currently selected.
         *
         * @param slotIndex the slot index
         * @return true, if is selected
         */
        @Override
        protected boolean isSelected(int slotIndex)
        {
            return slotIndex == this.selectedLayer;
        }

        /* (non-Javadoc)
         * @see net.minecraft.client.gui.GuiSlot#drawBackground()
         */
        @Override
        protected void drawBackground()
        {
        }

        /* (non-Javadoc)
         * @see net.minecraft.client.gui.GuiSlot#drawSlot(int, int, int, int, int, int, float)
         */
        @Override
        protected void drawSlot(int slotIndex, int xPos, int yPos, int heightIn, int mouseXIn, int mouseYIn, float partialTicks)
        {
            FlatLayerInfo flatlayerinfo = GuiCreateFlatWorldMod.this.generatorInfo.getFlatLayers().get(GuiCreateFlatWorldMod.this.generatorInfo.getFlatLayers().size() - slotIndex - 1);
            IBlockState iblockstate = flatlayerinfo.getLayerMaterial();
            Block block = iblockstate.getBlock();
            Item item = Item.getItemFromBlock(block);

            if (item == Items.AIR)
            {
                if (block != Blocks.WATER && block != Blocks.FLOWING_WATER)
                {
                    if (block == Blocks.LAVA || block == Blocks.FLOWING_LAVA)
                    {
                        item = Items.LAVA_BUCKET;
                    }
                }
                else
                {
                    item = Items.WATER_BUCKET;
                }
            }

            ItemStack itemstack = new ItemStack(item, 1, item.getHasSubtypes() ? block.getMetaFromState(iblockstate) : 0);
            String s = item.getItemStackDisplayName(itemstack);
            this.drawItem(xPos, yPos, itemstack);
            GuiCreateFlatWorldMod.this.fontRenderer.drawString(s, xPos + 18 + 5, yPos + 3, 16777215);
            String s1;

            if (slotIndex == 0)
            {
                s1 = I18n.format("createWorld.customize.flat.layer.top", flatlayerinfo.getLayerCount());
            }
            else if (slotIndex == GuiCreateFlatWorldMod.this.generatorInfo.getFlatLayers().size() - 1)
            {
                s1 = I18n.format("createWorld.customize.flat.layer.bottom", flatlayerinfo.getLayerCount());
            }
            else
            {
                s1 = I18n.format("createWorld.customize.flat.layer", flatlayerinfo.getLayerCount());
            }

            GuiCreateFlatWorldMod.this.fontRenderer.drawString(s1, xPos + 2 + 213 - GuiCreateFlatWorldMod.this.fontRenderer.getStringWidth(s1), yPos + 3, 16777215);
        }

        /* (non-Javadoc)
         * @see net.minecraft.client.gui.GuiSlot#getScrollBarX()
         */
        @Override
        protected int getScrollBarX()
        {
            return this.width - 70;
        }
    }
}