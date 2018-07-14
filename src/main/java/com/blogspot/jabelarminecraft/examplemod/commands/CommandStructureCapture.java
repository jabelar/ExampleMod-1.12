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

package com.blogspot.jabelarminecraft.examplemod.commands;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

// TODO: Auto-generated Javadoc
/**
 * This command is used to write information about every block in an area
 * to a file, in basically a simplified schematic format. This can be used in 
 * conjuction with a complementary build structure command that takes the file
 * and places the blocks to recreate the structure.
 * moved wrongly
 * @author jabelar
 *
 */
public class CommandStructureCapture extends CommandBase
{
    private final List<String> aliases;

    World theWorld;
    Entity thePlayer;
    public static String[][][] blockNameArray;
    public static int[][][] blockMetaArray;
    int startX;
    int startY;
    int startZ;
    int endX;
    int endY;
    int endZ;
    static int dimX;
    static int dimY;
    static int dimZ;
    static int signX;
    static int signY;
    static int signZ;

    /**
     * Instantiates a new command structure capture.
     */
    public CommandStructureCapture()
    {
        aliases = new ArrayList<String>();
        aliases.add("capture");
        aliases.add("capt");
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.minecraft.command.ICommand#getName()
     */
    @Override
    public String getName()
    {
        return "capture";
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.minecraft.command.ICommand#getUsage(net.minecraft.command.ICommandSender)
     */
    @Override
    public String getUsage(ICommandSender var1)
    {
        return "capture <int> <int> <int> <int> <int> <int> <text>"; // use "structure <text>"; later when passing name of structure
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.minecraft.command.ICommand#getAliases()
     */
    @Override
    public List<String> getAliases()
    {
        return this.aliases;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.minecraft.command.ICommand#execute(net.minecraft.server.MinecraftServer, net.minecraft.command.ICommandSender, java.lang.String[])
     */
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] argString)
    {
        theWorld = sender.getEntityWorld();

        if (theWorld.isRemote)
        {
            System.out.println("Not processing on Client side");
        }
        else
        {

            if (argString.length != 7)
            {
                sender.sendMessage(new TextComponentString("Invalid number of arguments"));
                return;
            }

            thePlayer = sender.getCommandSenderEntity();
            startX = Integer.parseInt(argString[0]);
            startY = Integer.parseInt(argString[1]);
            startZ = Integer.parseInt(argString[2]);
            endX = Integer.parseInt(argString[3]);
            endY = Integer.parseInt(argString[4]);
            endZ = Integer.parseInt(argString[5]);
            dimX = Math.abs(endX - startX);
            dimY = Math.abs(endY - startY);
            dimZ = Math.abs(endZ - startZ);
            if (endX < startX)
            {
                int temp = startX;
                startX = endX;
                endX = temp;
            }
            if (endY < startY)
            {
                int temp = startY;
                startY = endY;
                endY = temp;
            }
            if (endZ < startZ)
            {
                int temp = startZ;
                startZ = endZ;
                endZ = temp;
            }
            if (dimX * dimY * dimZ > 64 * 64 * 64)
            {
                sender.sendMessage(new TextComponentString("Capture area too big"));
                return;
            }

            sender.sendMessage(new TextComponentString("Capturing Structure from " + startX + ", " + startY + ", " +
                    startZ + " to " + endX + ", " + endY + ", " + endZ));
            blockNameArray = new String[dimX][dimY][dimZ];
            blockMetaArray = new int[dimX][dimY][dimZ];

            for (int indY = 0; indY < dimY; indY++) // Y first to organize in vertical layers
            {
                for (int indX = 0; indX < dimX; indX++)
                {
                    for (int indZ = 0; indZ < dimZ; indZ++)
                    {
                        BlockPos theBlockPos = new BlockPos(startX + indX, startY + indY, startZ + indZ);
                        blockNameArray[indX][indY][indZ] = Block.REGISTRY.getNameForObject(theWorld
                                .getBlockState(theBlockPos).getBlock()).toString();
                        blockMetaArray[indX][indY][indZ] = theWorld.getBlockState(theBlockPos)
                                .getBlock().getMetaFromState(theWorld.getBlockState(theBlockPos));
                    }
                }
            }

            writeFileNameArray(argString[6]);
        }
    }

    /**
     * Prints the name array.
     */
    protected void printNameArray()
    {
        System.out.println("// Block Name Array");
        System.out.println("{");
        for (int indY = 0; indY < dimY; indY++) // Y first to organize in layers
        {
            System.out.println("    {   // Layer =" + indY);
            for (int indX = 0; indX < dimX; indX++)
            {
                String row = "";
                for (int indZ = 0; indZ < dimZ; indZ++)
                {
                    if (indZ < dimZ - 1) // not last element in row
                    {
                        row = row + blockNameArray[indX][indY][indZ] + ", ";
                    }
                    else // last element in row
                    {
                        row = row + blockNameArray[indX][indY][indZ];
                    }
                }
                if (indX < dimX - 1) // not last element in column
                {
                    System.out.println("        { " + row + " },");
                }
                else // last element in column
                {
                    System.out.println("        { " + row + " }");
                }
            }
            if (indY < dimY - 1) // not last layer
            {
                System.out.println("    },");
            }
            else // last layer
            {
                System.out.println("    }");
            }
        }
        System.out.println("};");
    }

    /**
     * Prints the meta array.
     */
    protected void printMetaArray()
    {
        System.out.println("// Metadata Array");
        System.out.println("{");
        for (int indY = 0; indY < dimY; indY++) // Y first to organize in layers
        {
            System.out.println("    {   // Layer =" + indY);
            for (int indX = 0; indX < dimX; indX++)
            {
                String row = "";
                for (int indZ = 0; indZ < dimZ; indZ++)
                {
                    if (indZ < dimZ - 1) // not last element in row
                    {
                        row = row + blockMetaArray[indX][indY][indZ] + ", ";
                    }
                    else // last element in row
                    {
                        row = row + blockMetaArray[indX][indY][indZ];
                    }
                }
                if (indX < dimX - 1) // not last element in column
                {
                    System.out.println("        { " + row + " },");
                }
                else // last element in column
                {
                    System.out.println("        { " + row + " }");
                }
            }
            if (indY < dimY - 1) // not last layer
            {
                System.out.println("    },");
            }
            else // last layer
            {
                System.out.println("    }");
            }
        }
        System.out.println("};");
    }

    /**
     * Write file name array.
     *
     * @param fileName the file name
     */
    protected void writeFileNameArray(String fileName)
    {
        File file = new File(fileName + ".txt");
        PrintWriter printOut = null;
        try
        {
            printOut = new PrintWriter(new FileWriter(file));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        // print dimensions to the file
        if (printOut != null)
        {
            printOut.println(dimX);
            printOut.println(dimY);
            printOut.println(dimZ);
    
            // Write each string in the array on a separate line
            for (int indY = 0; indY < dimY; indY++) // Y first to organize in layers
            {
                for (int indX = 0; indX < dimX; indX++)
                {
                    for (int indZ = 0; indZ < dimZ; indZ++)
                    {
                        printOut.println(blockNameArray[indX][indY][indZ]);
                        printOut.println(blockMetaArray[indX][indY][indZ]);
                    }
                }
            }
            printOut.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.minecraft.command.ICommand#checkPermission(net.minecraft.server.MinecraftServer, net.minecraft.command.ICommandSender)
     */
    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender var1)
    {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.minecraft.command.ICommand#getTabCompletions(net.minecraft.server.MinecraftServer, net.minecraft.command.ICommandSender, java.lang.String[],
     * net.minecraft.util.math.BlockPos)
     */
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
            BlockPos targetPos)
    {
        return null;
    }
}
