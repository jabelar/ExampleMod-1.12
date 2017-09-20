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

package com.blogspot.jabelarminecraft.examplemod.utilities;

import java.util.List;

import com.blogspot.jabelarminecraft.examplemod.MainMod;
import com.blogspot.jabelarminecraft.examplemod.entities.IEntity;
import com.blogspot.jabelarminecraft.examplemod.networking.MessageSyncEntityToClient;
import com.blogspot.jabelarminecraft.examplemod.networking.MessageSyncEntityToServer;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;

// TODO: Auto-generated Javadoc
/**
 * @author jabelar
 *
 */
public class Utilities 
{ 
    // Need to call this on item instance prior to registering the item
    /**
     * Sets the item name.
     *
     * @param parItem the par item
     * @param parItemName the par item name
     * @return the item
     */
    // chainable
    public static Item setItemName(Item parItem, String parItemName) {
        parItem.setRegistryName(parItemName);
        parItem.setUnlocalizedName(parItemName);
        return parItem;
       } 
    
    // Need to call this on block instance prior to registering the block
    /**
     * Sets the block name.
     *
     * @param parBlock the par block
     * @param parBlockName the par block name
     * @return the block
     */
    // chainable
    public static Block setBlockName(Block parBlock, String parBlockName) {
        parBlock.setRegistryName(parBlockName);
        parBlock.setUnlocalizedName(parBlockName);
        return parBlock;
       }   

	/*
	 * Text Utilities
	 */
	
	/**
	 * String to rainbow.
	 *
	 * @param parString the par string
	 * @param parReturnToBlack the par return to black
	 * @return the string
	 */
	public static String stringToRainbow(String parString, boolean parReturnToBlack)
	{
		int stringLength = parString.length();
		if (stringLength < 1)
		{
			return "";
		}
		String outputString = "";
		TextFormatting[] colorChar = 
			{
			TextFormatting.RED,
			TextFormatting.GOLD,
			TextFormatting.YELLOW,
			TextFormatting.GREEN,
			TextFormatting.AQUA,
			TextFormatting.BLUE,
			TextFormatting.LIGHT_PURPLE,
			TextFormatting.DARK_PURPLE
			};
		for (int i = 0; i < stringLength; i++)
		{
			outputString = outputString+colorChar[i%8]+parString.substring(i, i+1);
		}
		// return color to a common one after (most chat is white, but for other GUI might want black)
		if (parReturnToBlack)
		{
			return outputString+TextFormatting.BLACK;
		}
		return outputString+TextFormatting.WHITE;
	}

	/**
	 * String to rainbow.
	 *
	 * @param parString the par string
	 * @return the string
	 */
	// by default return to white (for chat formatting).
	public static String stringToRainbow(String parString)
	{
		return stringToRainbow(parString, false);
	}
	
	/**
	 * String to golden.
	 *
	 * @param parString the par string
	 * @param parShineLocation the par shine location
	 * @param parReturnToBlack the par return to black
	 * @return the string
	 */
	public static String stringToGolden(String parString, int parShineLocation, boolean parReturnToBlack)
	{
		int stringLength = parString.length();
		if (stringLength < 1)
		{
			return "";
		}
		String outputString = "";
		for (int i = 0; i < stringLength; i++)
		{
			if ((i+parShineLocation+Minecraft.getSystemTime()/20)%88==0)
			{
				outputString = outputString+TextFormatting.WHITE+parString.substring(i, i+1);				
			}
			else if ((i+parShineLocation+Minecraft.getSystemTime()/20)%88==1)
			{
				outputString = outputString+TextFormatting.YELLOW+parString.substring(i, i+1);				
			}
			else if ((i+parShineLocation+Minecraft.getSystemTime()/20)%88==87)
			{
				outputString = outputString+TextFormatting.YELLOW+parString.substring(i, i+1);				
			}
			else
			{
				outputString = outputString+TextFormatting.GOLD+parString.substring(i, i+1);								
			}
		}
		// return color to a common one after (most chat is white, but for other GUI might want black)
		if (parReturnToBlack)
		{
			return outputString+TextFormatting.BLACK;
		}
		return outputString+TextFormatting.WHITE;
	}

	/**
	 * String to golden.
	 *
	 * @param parString the par string
	 * @param parShineLocation the par shine location
	 * @return the string
	 */
	// by default return to white (for chat formatting).
	public static String stringToGolden(String parString, int parShineLocation)
	{
		return stringToGolden(parString, parShineLocation, false);
	}
	
	/**
	* Based on code from http://pages.cs.wisc.edu/~ltorrey/cs302/examples/PigLatinTranslator.java
	* Method to translate a sentence word by word.
	* @param s The sentence in English
	* @return The pig latin version
	*/
	public static String toPigLatin(String s) 
	{
		String latin = "";
	    int i = 0;
	    while (i<s.length()) 
	    {
	    	// Take care of punctuation and spaces
	    	while (i<s.length() && !isLetter(s.charAt(i))) 
	    	{
	    		latin = latin + s.charAt(i);
	    		i++;
	    	}

	    	// If there aren't any words left, stop.
	    	if (i>=s.length()) break;

	    	// Otherwise we're at the beginning of a word.
	    	int begin = i;
	    	while (i<s.length() && isLetter(s.charAt(i))) 
	    	{
	    		i++;
	    	}

	    	// Now we're at the end of a word, so translate it.
	    	int end = i;
	    	latin = latin + pigWord(s.substring(begin, end));
	    }
	    return latin;
	}

	/**
	* Method to test whether a character is a letter or not.
	* @param c The character to test
	* @return True if it's a letter
	*/
	private static boolean isLetter(char c) 
	{
		return ( (c >='A' && c <='Z') || (c >='a' && c <='z') );
	}

	/**
	* Method to translate one word into pig latin.
	* @param word The word in english
	* @return The pig latin version
	*/
	private static String pigWord(String word) 
	{
		int split = firstVowel(word);
		return word.substring(split)+"-"+word.substring(0, split)+"ay";
	}

	/**
	* Method to find the index of the first vowel in a word.
	* @param word The word to search
	* @return The index of the first vowel
	*/
	private static int firstVowel(String word) 
	{
		word = word.toLowerCase();
	    for (int i=0; i<word.length(); i++)
	    {
	    	if (word.charAt(i)=='a' || word.charAt(i)=='e' ||
	    	      word.charAt(i)=='i' || word.charAt(i)=='o' ||
	              word.charAt(i)=='u')
	    	{
	    		return i;
	    	}
	    }
	    	return 0;
	}
	  
	/*
	 * Networking packet utilities
	 */
	
    /**
	 * Send entity sync packet to client.
	 *
	 * @param parEntity the par entity
	 */
	public static void sendEntitySyncPacketToClient(IEntity parEntity) 
    {
    	Entity theEntity = (Entity)parEntity;
        if (!theEntity.getEntityWorld().isRemote)
        {
        	// DEBUG
        	System.out.println("sendEntitySyncPacket from server for entity ID ="+theEntity.getEntityId());
            MainMod.network.sendToAll(new MessageSyncEntityToClient(theEntity.getEntityId(), parEntity.getSyncDataCompound()));           
        }
    }

    /**
     * Send entity sync packet to server.
     *
     * @param parEntity the par entity
     */
    public static void sendEntitySyncPacketToServer(IEntity parEntity) 
    {
    	Entity theEntity = (Entity)parEntity;
        if (theEntity.getEntityWorld().isRemote)
        {
        	// DEBUG
        	System.out.println("sendEntitySyncPacket from client");
            MainMod.network.sendToServer(new MessageSyncEntityToServer(theEntity.getEntityId(), parEntity.getSyncDataCompound()));           
        }
    }
    
    /*
     * World utilities
     */
    
    /**
     * Finds the topmost block position at an X, Z position in the world.
     *
     * @param parWorld the par world
     * @param parX the par X
     * @param parZ the par Z
     * @return the height value
     */
    public static double getHeightValue(World parWorld, double parX, double parZ)
    {
        int intX = MathHelper.floor(parX);
        int intZ = MathHelper.floor(parZ);

    	int chunkX = intX >> 4;
    	int chunkZ = intZ >> 4;
    	double height = parWorld.getChunkFromChunkCoords(chunkX, chunkZ)
    			.getHeightValue(intX & 15, intZ & 15);
    	
    	return height;
    }
    
    /**
     * Gets the mouse over extended.
     *
     * @param dist the dist
     * @return the mouse over extended
     */
    // This is mostly copied from the EntityRenderer#getMouseOver() method
    public static RayTraceResult getMouseOverExtended(float dist)
    {
        Minecraft mc = FMLClientHandler.instance().getClient();
        Entity theRenderViewEntity = mc.getRenderViewEntity();
        AxisAlignedBB theViewBoundingBox = new AxisAlignedBB(
                theRenderViewEntity.posX-0.5D,
                theRenderViewEntity.posY-0.0D,
                theRenderViewEntity.posZ-0.5D,
                theRenderViewEntity.posX+0.5D,
                theRenderViewEntity.posY+1.5D,
                theRenderViewEntity.posZ+0.5D
                );
        RayTraceResult returnMOP = null;
        if (mc.world != null)
        {
            double var2 = dist;
            returnMOP = theRenderViewEntity.rayTrace(var2, 0);
            double calcdist = var2;
            Vec3d pos = theRenderViewEntity.getPositionEyes(0);
            var2 = calcdist;
            if (returnMOP != null)
            {
                calcdist = returnMOP.hitVec.distanceTo(pos);
            }
            
            Vec3d lookvec = theRenderViewEntity.getLook(0);
            Vec3d var8 = pos.addVector(lookvec.x * var2, lookvec.y * var2, lookvec.z * var2);
            Entity pointedEntity = null;
            float var9 = 1.0F;
            @SuppressWarnings("unchecked")
            List<Entity> list = mc.world.getEntitiesWithinAABBExcludingEntity(theRenderViewEntity, theViewBoundingBox.grow(lookvec.x * var2, lookvec.y * var2, lookvec.z * var2).expand(var9, var9, var9));
            double d = calcdist;
            
            for (Entity entity : list)
            {
                if (entity.canBeCollidedWith())
                {
                    float bordersize = entity.getCollisionBorderSize();
                    AxisAlignedBB aabb = new AxisAlignedBB(entity.posX-entity.width/2, entity.posY, entity.posZ-entity.width/2, entity.posX+entity.width/2, entity.posY+entity.height, entity.posZ+entity.width/2);
                    aabb.expand(bordersize, bordersize, bordersize);
                    RayTraceResult mop0 = aabb.calculateIntercept(pos, var8);
                    
                    if (aabb.contains(pos))
                    {
                        if (0.0D < d || d == 0.0D)
                        {
                            pointedEntity = entity;
                            d = 0.0D;
                        }
                    } else if (mop0 != null)
                    {
                        double d1 = pos.distanceTo(mop0.hitVec);
                        
                        if (d1 < d || d == 0.0D)
                        {
                            pointedEntity = entity;
                            d = d1;
                        }
                    }
                }
            }
            
            if (pointedEntity != null && (d < calcdist || returnMOP == null))
            {
                returnMOP = new RayTraceResult(pointedEntity);
            }
        
        }
        return returnMOP;
    }
}

