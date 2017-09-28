package com.blogspot.jabelarminecraft.examplemod.utilities;

import com.blogspot.jabelarminecraft.examplemod.MainMod;

public class ModLogger 
{
	public static void print(String parString)
	{
		MainMod.logger.debug(parString);
		System.out.println(parString);
	}
}
