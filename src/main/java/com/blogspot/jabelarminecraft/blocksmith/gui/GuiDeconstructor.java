package com.blogspot.jabelarminecraft.blocksmith.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.blogspot.jabelarminecraft.blocksmith.containers.ContainerDeconstructor;
import com.blogspot.jabelarminecraft.blocksmith.containers.ContainerDeconstructor.State;

@SideOnly(Side.CLIENT)
public class GuiDeconstructor extends GuiContainer
{

    public ContainerDeconstructor container;
    private final String blockName;

    public GuiDeconstructor(InventoryPlayer playerInventory, World parWorld, String parBlockName, 
    		int parX, int parY, int parZ)
    {
        super(new ContainerDeconstructor(playerInventory, parWorld, parX, parY, parZ));
        container = (ContainerDeconstructor) inventorySlots;
        blockName = parBlockName;
    }

    @Override
	public void actionPerformed(GuiButton button)
    {
    }

    @Override
	public void drawScreen(int par1, int par2, float par3)
    {
        super.drawScreen(par1, par2, par3);
    }

    @Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        GL11.glDisable(GL11.GL_LIGHTING);

        fontRenderer.drawString(blockName, xSize / 2 - fontRenderer.getStringWidth(blockName) / 2 + 1, 5, 4210752);
        fontRenderer.drawString(I18n.format("container.inventory"), 6, ySize - 96 + 2, 4210752);

        String string = container.resultString;
        if(string != null)
        {
            State msgType = container.deconstructingState;
            TextFormatting format = TextFormatting.GREEN;
            TextFormatting shadowFormat = TextFormatting.DARK_GRAY;
            if(msgType == ContainerDeconstructor.State.ERROR)
            {
                format = TextFormatting.WHITE;
                shadowFormat = TextFormatting.DARK_RED;
            }

            fontRenderer.drawString(shadowFormat + string + TextFormatting.RESET, 6 + 1, ySize - 95 + 2 - fontRenderer.FONT_HEIGHT, 0);

            fontRenderer.drawString(format + string + TextFormatting.RESET, 6, ySize - 96 + 2 - fontRenderer.FONT_HEIGHT, 0);
        }

        GL11.glEnable(GL11.GL_LIGHTING);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glPushMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

        mc.renderEngine.bindTexture(new ResourceLocation("blocksmith:textures/gui/container/deconstructor.png"));

        int k = width / 2 - xSize / 2;
        int l = height / 2 - ySize / 2;
        drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
        GL11.glPopMatrix();
    }
}
