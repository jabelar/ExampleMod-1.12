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
package com.blogspot.jabelarminecraft.examplemod.init;

import net.minecraft.advancements.Advancement;

public class ModAdvancements
{
    // instantiate advancements
    public static Advancement advancementUseCompactor;

    /**
     * Register Advancements.
     */
    public static void registerAdvancements()
    {
        // BlockSmith.AdvancementTanningAHide = new Advancement("Advancement.tanningahide", "tanningahide", 0, 0, Items.LEATHER, (Advancement)null);
        // BlockSmith.AdvancementTanningAHide.registerStat().initIndependentStat(); // Eclipse is having trouble chaining these in previous line
        //// BlockSmith.AdvancementGiantSlayer = new Advancement("Advancement.giantslayer", "giantslayer", 2, 1, (Item)null, BlockSmith.AdvancementTanningAHide).setSpecial();
        //// BlockSmith.AdvancementGiantSlayer.registerStat(); // Eclipse is having trouble chaining this in previous line
        // BlockSmith.craftTable = new Advancement("createDecraftTable", "createDecraftTable", 1 - 2 - 2, -1 - 3, BlockSmith.blockDeconstructor, null).registerStat();
        // BlockSmith.deconstructAny = new Advancement("deconstructAnything", "deconstructAnything", 2 - 2, -2 - 2, Items.DIAMOND_HOE, BlockSmith.craftTable).registerStat();
        // BlockSmith.deconstructDiamondHoe = new Advancement("deconstructDiamondHoe", "deconstructDiamondHoe", 2 - 2, 0 - 2, Items.DIAMOND_HOE,
        // BlockSmith.deconstructAny).registerStat();
        // BlockSmith.deconstructJunk = new Advancement("deconstructJunk", "deconstructJunk", 1 - 2, -1 - 2, Items.LEATHER_BOOTS, BlockSmith.deconstructAny).registerStat();
        // BlockSmith.deconstructDiamondShovel = new Advancement("deconstructDiamondShovel", "deconstructDiamondShovel", 3 - 2, -1 - 2, Items.DIAMOND_SHOVEL,
        // BlockSmith.deconstructAny).registerStat();
        // BlockSmith.theHatStandAdvancement = new Advancement("porteManteauAdvancement", "porteManteauAdvancement", 3 - 2, -4 - 2, Blocks.OAK_FENCE,
        // BlockSmith.craftTable).registerStat();
        // AdvancementPage.registerAdvancementPage(new AdvancementPage("BlockSmith",
        // new Advancement[]
        // {
        // BlockSmith.craftTable, BlockSmith.deconstructAny, BlockSmith.deconstructDiamondHoe, BlockSmith.deconstructJunk, BlockSmith.deconstructDiamondShovel,
        // BlockSmith.theHatStandAdvancement
        // }));
        //
        // BlockSmith.deconstructedItemsStat = (StatBasic) (new StatBasic("stat.deconstructeditems", new TextComponentTranslation("stat.deconstructeditems", new
        // Object[0])).registerStat());
        //
    }

}
