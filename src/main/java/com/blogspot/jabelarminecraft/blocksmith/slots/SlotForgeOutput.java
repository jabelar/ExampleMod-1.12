/**
 * 
 */
package com.blogspot.jabelarminecraft.blocksmith.slots;

import com.blogspot.jabelarminecraft.blocksmith.recipes.ForgeRecipes;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

/**
 * @author jabelar
 *
 */
public class SlotForgeOutput extends SlotOutput
{
    public SlotForgeOutput(EntityPlayer parPlayer, IInventory parIInventory, int parSlotIndex, int parXDisplayPosition, int parYDisplayPosition)
    {
        super(parPlayer, parIInventory, parSlotIndex, parXDisplayPosition, parYDisplayPosition);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
     */
    @Override
	protected void onCrafting(ItemStack parItemStack)
    {
        parItemStack.onCrafting(thePlayer.world, thePlayer, getNumOutput());

        if (!thePlayer.world.isRemote)
        {
            int i = getNumOutput();
            float f = ForgeRecipes.instance().getSmeltingExperience(parItemStack);
            int j;

            if (f == 0.0F)
            {
                i = 0;
            }
            else if (f < 1.0F)
            {
                j = MathHelper.floor(i * f);

                if (j < MathHelper.ceil(i * f) && Math.random() < i * f - j)
                {
                    ++j;
                }

                i = j;
            }

            while (i > 0)
            {
                j = EntityXPOrb.getXPSplit(i);
                i -= j;
                thePlayer.world.spawnEntity(new EntityXPOrb(thePlayer.world, thePlayer.posX, thePlayer.posY + 0.5D, thePlayer.posZ + 0.5D, j));
            }
        }

        setNumOutput(0);

        net.minecraftforge.fml.common.FMLCommonHandler.instance().firePlayerSmeltedEvent(thePlayer, parItemStack);

        if (parItemStack.getItem() == Items.IRON_INGOT)
        {
//           thePlayer.addStat(AchievementList.ACQUIRE_IRON);
        }

        if (parItemStack.getItem() == Items.COOKED_FISH)
        {
//            thePlayer.addStat(AchievementList.COOK_FISH);
        }
    }
}