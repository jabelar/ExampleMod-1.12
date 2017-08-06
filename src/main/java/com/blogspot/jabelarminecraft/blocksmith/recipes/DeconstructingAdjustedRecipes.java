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

package com.blogspot.jabelarminecraft.blocksmith.recipes;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * @author jabelar
 *
 */
public class DeconstructingAdjustedRecipes 
{
    // Counters to allow fractional deconstruction
    public int divideByTwoCounter = 1;
    public int divideByThreeCounter = 2;
    public int divideByFourCounter = 3;
    public int divideByEightCounter = 7;
    
    // The item and meta data input to deconstructor
    public Item theItem = null;
    public int theMetadata = 0;
    
    public DeconstructingAdjustedRecipes()
    {
        
    }

    /**
     * Adjust those cases where the recipe can be divided down (e.g. one door gives back two blocks)
     * @param parOutputItemStackArray should hold the regular recipe output item stack array
     * @param theItem 
     */
    public ItemStack[] adjustOutputQuantities(ItemStack[] parOutputItemStackArray, ItemStack parInputItemStack) 
    {
        theItem = parInputItemStack.getItem();
        theMetadata = theItem.getMetadata(parInputItemStack);
        if (theItem == Items.OAK_DOOR) return outputForWoodenDoor(0);
        if (theItem == Items.SPRUCE_DOOR) return outputForWoodenDoor(1);
        else if (theItem == Items.BIRCH_DOOR) return outputForWoodenDoor(2);
        else if (theItem == Items.JUNGLE_DOOR) return outputForWoodenDoor(3);
        else if (theItem == Items.ACACIA_DOOR) return outputForWoodenDoor(4);
        else if (theItem == Items.DARK_OAK_DOOR) return outputForWoodenDoor(5);
        else if (theItem == Items.IRON_DOOR)
        {
            return new ItemStack[] {
                    new ItemStack(Items.IRON_INGOT, 1, 0),
                    new ItemStack(Items.IRON_INGOT, 1, 0),
                    null, null, null, null, null, null, null
            };
        }
        else if (theItem == Items.PAPER) return outputSingle(Items.REEDS);
        else if (theItem == Items.STICK) return outputSingle(Blocks.PLANKS);
        else if (theItem == Item.getItemFromBlock(Blocks.LADDER))
        {
            if (divideByThreeCounter <= 0)
            {
                decrementDivideByThreeCounter();
                return new ItemStack[] {
                        null, null, null,
                        new ItemStack(Items.STICK, 1, 0),
                        new ItemStack(Items.STICK, 1, 0), 
                        new ItemStack(Items.STICK, 1, 0), 
                        null, null, null
                };
            }
            else if (divideByThreeCounter == 1)
            {
                decrementDivideByThreeCounter();
                return new ItemStack[] {
                        new ItemStack(Items.STICK, 1, 0),
                        null,
                        new ItemStack(Items.STICK, 1, 0), 
                        null, null, null, null, null, null
                };
            }
            else if (divideByThreeCounter == 2)
            {
                decrementDivideByThreeCounter();
                return new ItemStack[] {
                        null, null, null, null, null, null,
                        new ItemStack(Items.STICK, 1, 0),
                        null,
                        new ItemStack(Items.STICK, 1, 0), 
                };
            }
        }
        else if (theItem == Item.getItemFromBlock(Blocks.OAK_FENCE)) return outputForWoodenFence(0);
        else if (theItem == Item.getItemFromBlock(Blocks.SPRUCE_FENCE)) return outputForWoodenFence(1);
        else if (theItem == Item.getItemFromBlock(Blocks.BIRCH_FENCE)) return outputForWoodenFence(2);
        else if (theItem == Item.getItemFromBlock(Blocks.JUNGLE_FENCE)) return outputForWoodenFence(3);
        else if (theItem == Item.getItemFromBlock(Blocks.ACACIA_FENCE)) return outputForWoodenFence(4);
        else if (theItem == Item.getItemFromBlock(Blocks.DARK_OAK_FENCE)) return outputForWoodenFence(5);
        else if (theItem == Items.ENCHANTED_BOOK)
        {
            return new ItemStack[] {
                    null, 
                    new ItemStack(Items.REEDS, 1, 0),
                    null,
                    null, 
                    new ItemStack(Items.REEDS, 1, 0),
                    null,
                    null, 
                    new ItemStack(Items.REEDS, 1, 0),
                    null
            };
        }
        else if (theItem == Item.getItemFromBlock(Blocks.NETHER_BRICK_FENCE)) return outputSingle(Blocks.NETHER_BRICK);
        else if (theItem == Item.getItemFromBlock(Blocks.WOODEN_SLAB)) return outputSingle(Blocks.PLANKS, theMetadata);
        else if (theItem == Item.getItemFromBlock(Blocks.STONE_SLAB)) return outputForStoneSlab();
        else if (theItem == Item.getItemFromBlock(Blocks.STONE_SLAB2)) return outputSingle(Blocks.RED_SANDSTONE);
        else if (theItem == Items.SIGN)
        {
            ItemStack PLANKSItemStack = new ItemStack(Item.getItemFromBlock(Blocks.PLANKS), 1, 0);
            if (divideByThreeCounter == 2)
            {
                decrementDivideByThreeCounter();
                return new ItemStack[] {
                        PLANKSItemStack, null, null,
                        PLANKSItemStack, null, null, 
                        null, null, null
                };
            }
            else if (divideByThreeCounter == 0)
            {
                decrementDivideByThreeCounter();
                return new ItemStack[] {
                        null, PLANKSItemStack, null, 
                        null, PLANKSItemStack, null, 
                        null, new ItemStack(Items.STICK, 1, 0), null
                };
            }
            else if (divideByThreeCounter == 1)
            {
                decrementDivideByThreeCounter();
                return new ItemStack[] {
                        null, null, PLANKSItemStack, 
                        null, null, PLANKSItemStack,
                        null, null, null
                };
            }
        }
        else if (theItem == Items.GLASS_BOTTLE) return outputSingle(Blocks.GLASS);
        else if (theItem == Item.getItemFromBlock(Blocks.RAIL))
        {
            // DEBUG
            System.out.println("Divide by two counter = "+divideByTwoCounter);
            if (divideByTwoCounter == 1)
            {
                decrementDivideByTwoCounter();
                return new ItemStack[] {
                        new ItemStack(Items.IRON_INGOT, 1, 0), null, null,
                        new ItemStack(Items.IRON_INGOT, 1, 0), null, null,
                        new ItemStack(Items.IRON_INGOT, 1, 0), null, null
                };
            }
            else if (divideByTwoCounter == 0)
            {
                decrementDivideByTwoCounter();
                return new ItemStack[] {
                        null, null, new ItemStack(Items.IRON_INGOT, 1, 0),
                        null, new ItemStack(Items.STICK, 1, 0), new ItemStack(Items.IRON_INGOT, 1, 0),
                        null, null, new ItemStack(Items.IRON_INGOT, 1, 0)
                };
            }
        }
        else if (theItem == Item.getItemFromBlock(Blocks.GOLDEN_RAIL))
        {
            // DEBUG
            System.out.println("Divide by two counter = "+divideByTwoCounter);
            if (divideByTwoCounter == 1)
            {
                decrementDivideByTwoCounter();
                return new ItemStack[] {
                        new ItemStack(Items.GOLD_INGOT, 1, 0), null, null,
                        new ItemStack(Items.GOLD_INGOT, 1, 0), new ItemStack(Items.STICK, 1, 0), null,
                        new ItemStack(Items.GOLD_INGOT, 1, 0), null, null
                };
            }
            else if (divideByTwoCounter == 0)
            {
                decrementDivideByTwoCounter();
                return new ItemStack[] {
                        null, null, new ItemStack(Items.GOLD_INGOT, 1, 0),
                        null, null, new ItemStack(Items.GOLD_INGOT, 1, 0),
                        null, new ItemStack(Items.REDSTONE), new ItemStack(Items.GOLD_INGOT, 1, 0)
                };
            }
        }
        else if (theItem == Item.getItemFromBlock(Blocks.ACTIVATOR_RAIL))
        {
            // DEBUG
            System.out.println("Divide by two counter = "+divideByTwoCounter);
            if (divideByTwoCounter == 1)
            {
                decrementDivideByTwoCounter();
                return new ItemStack[] {
                        new ItemStack(Items.IRON_INGOT, 1, 0), new ItemStack(Items.STICK, 1, 0), null,
                        new ItemStack(Items.IRON_INGOT, 1, 0), null, null,
                        new ItemStack(Items.IRON_INGOT, 1, 0), null, null
                };
            }
            else if (divideByTwoCounter == 0)
            {
                decrementDivideByTwoCounter();
                return new ItemStack[] {
                        null, null, new ItemStack(Items.IRON_INGOT, 1, 0),
                        null, new ItemStack(Item.getItemFromBlock(Blocks.REDSTONE_TORCH), 1, 0), new ItemStack(Items.IRON_INGOT, 1, 0),
                        null, new ItemStack(Items.STICK, 1, 0), new ItemStack(Items.IRON_INGOT, 1, 0)
                };
            }
        }
        else if (theItem == Item.getItemFromBlock(Blocks.DETECTOR_RAIL))
        {
            // DEBUG
            System.out.println("Divide by two counter = "+divideByTwoCounter);
            if (divideByTwoCounter == 1)
            {
                decrementDivideByTwoCounter();
                return new ItemStack[] {
                        new ItemStack(Items.IRON_INGOT, 1, 0), null, null,
                        new ItemStack(Items.IRON_INGOT, 1, 0), new ItemStack(Item.getItemFromBlock(Blocks.STONE_PRESSURE_PLATE), 1, 0), null,
                        new ItemStack(Items.IRON_INGOT, 1, 0), null, null
                };
            }
            else if (divideByTwoCounter == 0)
            {
                decrementDivideByTwoCounter();
                return new ItemStack[] {
                        null, null, new ItemStack(Items.IRON_INGOT, 1, 0),
                        null, null, new ItemStack(Items.IRON_INGOT, 1, 0),
                        null, new ItemStack(Items.REDSTONE), new ItemStack(Items.IRON_INGOT, 1, 0)
                };
            }
        }
        else if (theItem == Item.getItemFromBlock(Blocks.GLASS_PANE))
        {
            return new ItemStack[] {
                    null, null, null, null, null, null,
                    new ItemStack(Item.getItemFromBlock(Blocks.GLASS), 1, 0), 
                    new ItemStack(Item.getItemFromBlock(Blocks.GLASS), 1, 0), 
                    new ItemStack(Item.getItemFromBlock(Blocks.GLASS), 1, 0)
            };
        }
        else if (theItem == Item.getItemFromBlock(Blocks.STAINED_GLASS_PANE))
        {
            return new ItemStack[] {
                    null, null, null, null, null, null,
                    new ItemStack(Item.getItemFromBlock(Blocks.STAINED_GLASS), 1, theMetadata), 
                    new ItemStack(Item.getItemFromBlock(Blocks.STAINED_GLASS), 1, theMetadata), 
                    new ItemStack(Item.getItemFromBlock(Blocks.STAINED_GLASS), 1, theMetadata)
            };
        }
        else if (theItem == Item.getItemFromBlock(Blocks.COBBLESTONE_WALL)) return outputSingle(Blocks.COBBLESTONE);
        else if (theItem == Item.getItemFromBlock(Blocks.QUARTZ_BLOCK)) return outputForQuartz();
        else if (theItem == Item.getItemFromBlock(Blocks.STAINED_HARDENED_CLAY)) return outputForHardenedClay();
        // Wooden stairs
        else if (theItem == Item.getItemFromBlock(Blocks.OAK_STAIRS)) return outputForStairs(new ItemStack(Item.getItemFromBlock(Blocks.PLANKS), 1, 0));
        else if (theItem == Item.getItemFromBlock(Blocks.SPRUCE_STAIRS)) return outputForStairs(new ItemStack(Item.getItemFromBlock(Blocks.PLANKS), 1, 1));
        else if (theItem == Item.getItemFromBlock(Blocks.BIRCH_STAIRS)) return outputForStairs(new ItemStack(Item.getItemFromBlock(Blocks.PLANKS), 1, 2));
        else if (theItem == Item.getItemFromBlock(Blocks.JUNGLE_STAIRS)) return outputForStairs(new ItemStack(Item.getItemFromBlock(Blocks.PLANKS), 1, 3));
        else if (theItem == Item.getItemFromBlock(Blocks.ACACIA_STAIRS)) return outputForStairs(new ItemStack(Item.getItemFromBlock(Blocks.PLANKS), 1, 4));
        else if (theItem == Item.getItemFromBlock(Blocks.DARK_OAK_STAIRS)) return outputForStairs(new ItemStack(Item.getItemFromBlock(Blocks.PLANKS), 1, 5));
        // Stone stairs
        else if (theItem == Item.getItemFromBlock(Blocks.SANDSTONE_STAIRS)) return outputForStairs(new ItemStack(Item.getItemFromBlock(Blocks.SANDSTONE)));
        else if (theItem == Item.getItemFromBlock(Blocks.STONE_STAIRS)) return outputForStairs(new ItemStack(Item.getItemFromBlock(Blocks.COBBLESTONE)));
        else if (theItem == Item.getItemFromBlock(Blocks.BRICK_STAIRS)) return outputForStairs(new ItemStack(Item.getItemFromBlock(Blocks.BRICK_BLOCK)));
        else if (theItem == Item.getItemFromBlock(Blocks.NETHER_BRICK_STAIRS)) return outputForStairs(new ItemStack(Item.getItemFromBlock(Blocks.NETHER_BRICK)));
        else if (theItem == Item.getItemFromBlock(Blocks.RED_SANDSTONE_STAIRS)) return outputForStairs(new ItemStack(Item.getItemFromBlock(Blocks.RED_SANDSTONE)));
        else if (theItem == Item.getItemFromBlock(Blocks.STONE_BRICK_STAIRS)) return outputForStairs(new ItemStack(Item.getItemFromBlock(Blocks.STONEBRICK)));
        else if (theItem == Item.getItemFromBlock(Blocks.QUARTZ_STAIRS)) return outputForStairs(new ItemStack(Item.getItemFromBlock(Blocks.QUARTZ_BLOCK)));

        // else no adjustments needed
        return parOutputItemStackArray ;
    }
    
    private ItemStack[] outputSingle(Block parBlock)
    {
        return new ItemStack[] {
                new ItemStack(Item.getItemFromBlock(parBlock)),
                null, null, null, null, null, null, null, null
        };
    }

    private ItemStack[] outputSingle(Item parItem)
    {
        return new ItemStack[] {
                new ItemStack(parItem),
                null, null, null, null, null, null, null, null
        };
    }
        
    private ItemStack[] outputSingle(Block parBlock, int parMetadata)
    {
        return new ItemStack[] {
                new ItemStack(Item.getItemFromBlock(parBlock), 1, parMetadata),
                null, null, null, null, null, null, null, null
        };
    }

    private ItemStack[] outputSingle(Item parItem, int parMetadata)
    {
        return new ItemStack[] {
                new ItemStack(parItem, 1, parMetadata),
                null, null, null, null, null, null, null, null
        };
    }

    private ItemStack[] outputForWoodenDoor(int parMetadata)
    {
        return new ItemStack[] {
                new ItemStack(Item.getItemFromBlock(Blocks.PLANKS), 1, parMetadata),
                new ItemStack(Item.getItemFromBlock(Blocks.PLANKS), 1, parMetadata),
                null, null, null, null, null, null, null
        };
    }

    private ItemStack[] outputForWoodenFence(int parMetadata)
    {
        ItemStack[] resultItemStackArray = initItemStackArray();
        ItemStack PLANKSItemStack = new ItemStack(Item.getItemFromBlock(Blocks.PLANKS), 1, parMetadata);
        if (divideByThreeCounter == 2)
        {
            decrementDivideByThreeCounter();
            resultItemStackArray = new ItemStack[] {
                    null, null, null,
                    PLANKSItemStack,
                    new ItemStack(Items.STICK, 1, 0), 
                    null, null, null, null
            };
        }
        else if (divideByThreeCounter == 1)
        {
            decrementDivideByThreeCounter();
            resultItemStackArray = new ItemStack[] {
                    null, null, null, null, null, null, null,
                    new ItemStack(Items.STICK, 1, 0), 
                    PLANKSItemStack
            };
        }
        else if (divideByThreeCounter == 0)
        {
            decrementDivideByThreeCounter();
            resultItemStackArray = new ItemStack[] {
                    null, null, null, null, null,
                    PLANKSItemStack,
                    PLANKSItemStack,
                    null, null
            };
        }
        return resultItemStackArray;
    }

    
    private ItemStack[] outputForStoneSlab()
    {
        ItemStack[] resultItemStackArray = initItemStackArray();
        // Need to handle all the various subtypes
        // Also need to handle upper and lower slabs (this is why I do bitwise mask with 7)
        if ((theMetadata&7) == 0)
        {
            resultItemStackArray = new ItemStack[] {
                    new ItemStack(Item.getItemFromBlock(Blocks.STONE), 1, 0),
                    null, null, null, null, null, null, null, null
            };
        }
        else if ((theMetadata&7) == 1)
        {
            resultItemStackArray = new ItemStack[] {
                    new ItemStack(Item.getItemFromBlock(Blocks.SANDSTONE), 1, 0),
                    null, null, null, null, null, null, null, null
            };
        }
        else if ((theMetadata&7) == 2) // this is supposed to be "(STONE) wooden slab" which I don't know what that is
        {
            resultItemStackArray = new ItemStack[] {
                    new ItemStack(Item.getItemFromBlock(Blocks.STONE), 1, 0),
                    null, null, null, null, null, null, null, null
            };
        }
        else if ((theMetadata&7) == 3)
        {
            resultItemStackArray = new ItemStack[] {
                    new ItemStack(Item.getItemFromBlock(Blocks.COBBLESTONE), 1, 0),
                    null, null, null, null, null, null, null, null
            };
        }
        else if ((theMetadata&7) == 4)
        {
            resultItemStackArray = new ItemStack[] {
                    new ItemStack(Item.getItemFromBlock(Blocks.BRICK_BLOCK), 1, 0),
                    null, null, null, null, null, null, null, null
            };
        }
        else if ((theMetadata&7) == 5)
        {
            resultItemStackArray = new ItemStack[] {
                    new ItemStack(Item.getItemFromBlock(Blocks.STONEBRICK), 1, 0),
                    null, null, null, null, null, null, null, null
            };
        }
        else if ((theMetadata&7) == 6)
        {
            resultItemStackArray = new ItemStack[] {
                    new ItemStack(Item.getItemFromBlock(Blocks.NETHER_BRICK), 1, 0),
                    null, null, null, null, null, null, null, null
            };
        }
        else if ((theMetadata&7) == 7)
        {
            resultItemStackArray = new ItemStack[] {
                    new ItemStack(Item.getItemFromBlock(Blocks.QUARTZ_BLOCK), 1, 0),
                    null, null, null, null, null, null, null, null
            };
        }
        return resultItemStackArray;
    }

    private ItemStack[] outputForQuartz()
    {
        ItemStack[] resultItemStackArray = initItemStackArray();
        if (theMetadata == 0) // regular QUARTZ block
        {
            resultItemStackArray = new ItemStack[] {
                    null, null, null,
                    new ItemStack(Items.QUARTZ, 1, 0), new ItemStack(Items.QUARTZ, 1, 0), null,
                    new ItemStack(Items.QUARTZ, 1, 0), new ItemStack(Items.QUARTZ, 1, 0), null
            };
        }
        else if (theMetadata == 1) // chizeled QUARTZ block
        {
            resultItemStackArray = new ItemStack[] {
                    null, null, null,
                    null, new ItemStack(Item.getItemFromBlock(Blocks.STONE_SLAB), 1, 7), null,
                    null, new ItemStack(Item.getItemFromBlock(Blocks.STONE_SLAB), 1, 7), null
            };
        }
        else if (theMetadata == 2 || theMetadata == 3 || theMetadata == 4) // pillar QUARTZ block, any orientation
        {
            if (divideByTwoCounter == 1)
            {
                decrementDivideByTwoCounter();
                resultItemStackArray = new ItemStack[] {
                        null, null, null,
                        null, null, null,
                        null, new ItemStack(Item.getItemFromBlock(Blocks.QUARTZ_BLOCK), 1, 0), null
                };
            }
            else if (divideByTwoCounter == 0)
            {
                decrementDivideByTwoCounter();
                resultItemStackArray = new ItemStack[] {
                        null, null, null,
                        null, new ItemStack(Item.getItemFromBlock(Blocks.QUARTZ_BLOCK), 1, 0), null,
                        null, null, null
                };
            }
        }
        return resultItemStackArray;
    }

    private ItemStack[] outputForHardenedClay()
    {
        if (divideByEightCounter != 3) 
        {
            decrementDivideByEightCounter();
            return new ItemStack[] {
                    new ItemStack(Item.getItemFromBlock(Blocks.CLAY), 1, 0), null, null,
                    null, null, null,
                    null, null, null
            };
        }
        else 
        {
            // DEBUG
            System.out.println("Should output a DYE");
            decrementDivideByEightCounter();
            return new ItemStack[] {
                    new ItemStack(Item.getItemFromBlock(Blocks.CLAY), 1, 0), new ItemStack(Items.DYE, 1, convertClayMetaToDyeMeta(theMetadata)), null,
                    null, null, null,
                    null, null, null
            };
        }
    }
    
    private ItemStack[] outputForStairs(ItemStack parOutputItemStack)
    { 
        ItemStack[] resultItemStackArray = initItemStackArray();
        if (divideByFourCounter == 0) 
        {
            decrementDivideByFourCounter();
            resultItemStackArray = new ItemStack[] {
                    null, null, parOutputItemStack,
                    null, null, null,
                    null, null, null
            };
        }
        else if (divideByFourCounter == 1)
        {
            decrementDivideByFourCounter();
            resultItemStackArray = new ItemStack[] {
                    null, null, null,
                    null, parOutputItemStack, parOutputItemStack,
                    null, null, null
            };
        }
        else if (divideByFourCounter == 2)
        {
            decrementDivideByFourCounter();
            resultItemStackArray = new ItemStack[] {
                    null, null, null,
                    null, null, null,
                    null, null, parOutputItemStack
            };
        }
        else if (divideByFourCounter == 3)
        {
            decrementDivideByFourCounter();
            resultItemStackArray = new ItemStack[] {
                    null, null, null,
                    null, null, null,
                    parOutputItemStack, parOutputItemStack, null
            };
        }
    
        return resultItemStackArray;
    }

    private int convertClayMetaToDyeMeta(int parClayMeta)
    {
        // for some reason DYE and CLAY have reversed sequence of meta data values
        return 15-parClayMeta;
    }
    
    private void decrementDivideByTwoCounter()
    {
        divideByTwoCounter--;
        if (divideByTwoCounter<0)
        {
            divideByTwoCounter=1;
        }                
    }
    
    private void decrementDivideByThreeCounter()
    {
        divideByThreeCounter--;
        if (divideByThreeCounter<0)
        {
            divideByThreeCounter=2;
        }                
    }
    
    private void decrementDivideByFourCounter()
    {
        divideByFourCounter--;
        if (divideByFourCounter<0)
        {
            divideByFourCounter=3;
        }                
    }
    
    private void decrementDivideByEightCounter()
    {
        divideByEightCounter--;
        if (divideByEightCounter<0)
        {
            divideByEightCounter=7;
        }                
    }
    
    private ItemStack[] initItemStackArray()
    {
        ItemStack[] resultItemStackArray = new ItemStack[9];
        for(int j = 0;j<resultItemStackArray.length;j++)
        {
            resultItemStackArray[j] = null;
        }
        return resultItemStackArray;
    }
}
