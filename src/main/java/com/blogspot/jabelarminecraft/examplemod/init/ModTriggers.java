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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.blogspot.jabelarminecraft.examplemod.advancements.CustomTrigger;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

/**
 * This class is part of my simple custom advancement triggering tutorial.
 * See: http://jabelarminecraft.blogspot.com/p/minecraft-modding-custom-triggers-aka.html
 * 
 * @author jabelar
 */
public class ModTriggers

{
    public static final CustomTrigger PLACE_CLOUD_SAPLING = new CustomTrigger("place_cloud_sapling");

    /*
     * This array just makes it convenient to register all the criteria.
     */
    public static final CustomTrigger[] TRIGGER_ARRAY = new CustomTrigger[] {
            PLACE_CLOUD_SAPLING
            };


    /**
     * This method is part of my simple custom advancement triggering tutorial.
     * See: http://jabelarminecraft.blogspot.com/p/minecraft-modding-custom-triggers-aka.html
     */
    public static void registerTriggers()
    {
        // DEBUG
        System.out.println("Registering custom triggers");
        
        Method method;

        method = ReflectionHelper.findMethod(CriteriaTriggers.class, "register", "func_192118_a", ICriterionTrigger.class);

        method.setAccessible(true);

        for (int i=0; i < TRIGGER_ARRAY.length; i++)
        {
             try
            {
                method.invoke(null, TRIGGER_ARRAY[i]);
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } 
    }
}

