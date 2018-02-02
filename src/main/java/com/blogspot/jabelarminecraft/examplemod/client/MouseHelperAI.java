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

package com.blogspot.jabelarminecraft.examplemod.client;

import net.minecraft.util.MouseHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

// TODO: Auto-generated Javadoc
/**
 * @author jabelar
 *
 */
public class MouseHelperAI extends MouseHelper
{
    
    /**
     * Instantiates a new mouse helper AI.
     */
    public MouseHelperAI()
    {
        super();
        // DEBUG
        System.out.println("Constructing MouseHelper for AI bots");
    }
    
    /* (non-Javadoc)
     * @see net.minecraft.util.MouseHelper#mouseXYChange()
     */
    @Override
    public void mouseXYChange()
    {
        if (Keyboard.isKeyDown(Keyboard.KEY_COMMA))
        {
            this.deltaX += 1;
        }
        else
        {
            this.deltaX = Mouse.getDX();
        }
        this.deltaY = Mouse.getDY();
    }
}