package com.blogspot.jabelarminecraft.blocksmith.containers;

import com.blogspot.jabelarminecraft.blocksmith.BlockSmith;
import com.blogspot.jabelarminecraft.blocksmith.recipes.DeconstructingInputQuantity;
import com.blogspot.jabelarminecraft.blocksmith.recipes.DeconstructingRecipeHandler;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerDeconstructor extends Container
{

    public static enum State
    {
        ERROR, READY
    }

    public InventoryCrafting inputInventory = new InventoryCrafting(this, 1, 1);
    public int inputSlotNumber;
    public InventoryDeconstructResult outputInventory = new InventoryDeconstructResult();
    public DeconstructingRecipeHandler deconstructingRecipeHandler;
    private final World worldObj;
    public InventoryPlayer playerInventory;
    public String resultString = "deconstructing.result.ready";
    public State deconstructingState = State.READY;
    public int x = 0;
    public int y = 0;
    public int z = 0;

    public ContainerDeconstructor(InventoryPlayer parPlayerInventory, World parWorld, int parX, int parY, int parZ)
    {
        x = parX;
        y = parY;
        z = parZ;
        worldObj = parWorld;
        
        deconstructingRecipeHandler = new DeconstructingRecipeHandler();
        
        for(int outputSlotIndexX = 0; outputSlotIndexX < 3; ++outputSlotIndexX)
        {
            for(int outputSlotIndexY = 0; outputSlotIndexY < 3; ++outputSlotIndexY)
            {
                addSlotToContainer(new Slot(outputInventory, outputSlotIndexY + outputSlotIndexX * 3, 112 + outputSlotIndexY * 18, 17 + outputSlotIndexX * 18));
            }
        }
        
        inputSlotNumber = addSlotToContainer(new Slot(inputInventory, 0, 30 + 15, 35)).slotNumber;

        for(int playerSlotIndexY = 0; playerSlotIndexY < 3; ++playerSlotIndexY)
        {
            for(int playerSlotIndexX = 0; playerSlotIndexX < 9; ++playerSlotIndexX)
            {
                addSlotToContainer(new Slot(parPlayerInventory, playerSlotIndexX + playerSlotIndexY * 9 + 9, 8 + playerSlotIndexX * 18, 84 + playerSlotIndexY * 18));
            }
        }
        
        for(int hotbarSlotIndex = 0; hotbarSlotIndex < 9; ++hotbarSlotIndex)
        {
            addSlotToContainer(new Slot(parPlayerInventory, hotbarSlotIndex, 8 + hotbarSlotIndex * 18, 142));
        }     
        
        playerInventory = parPlayerInventory;
    }

    @Override
    public void onCraftMatrixChanged(IInventory parInventory)
    {
    	if(parInventory == inputInventory)
        {
            if(inputInventory.getStackInSlot(0) == null)
            {
                resultString = I18n.format("deconstructing.result.ready");
                deconstructingState = State.READY;
                return;
            }
            int amountRequired = DeconstructingInputQuantity.getStackSizeNeeded(inputInventory.getStackInSlot(0));
            // DEBUG
            System.out.println("Amount required = "+amountRequired);

            if(amountRequired > inputInventory.getStackInSlot(0).getCount())
            {
                resultString = I18n.format("deconstructing.result.needMoreStacks", (amountRequired - inputInventory.getStackInSlot(0).getCount()));
                deconstructingState = State.ERROR;
                return;
            }
            
            if (amountRequired <= 0)
            {
                resultString = I18n.format("deconstructing.result.impossible");
                deconstructingState = State.ERROR;
                return;
            }
            
            ItemStack[] outputItemStackArray = deconstructingRecipeHandler.getDeconstructResults(inputInventory.getStackInSlot(0));
            NonNullList<ItemStack> outputItemStackArray = NonNull
            if (outputItemStackArray == null)
            {
                resultString = I18n.format("deconstructing.result.impossible");
                deconstructingState = State.ERROR;
                return;
            }
           
            // Loop while there is something in the input slot with sufficient amount
            while(inputInventory.getStackInSlot(0) != null && amountRequired > 0 && amountRequired <= inputInventory.getStackInSlot(0).getCount())
            {                              
                if(!outputInventory.isEmpty())
                {
                    for(int i = 0; i < outputInventory.getSizeInventory(); i++ )
                    {
                        ItemStack itemStackInOutputSlot = outputInventory.getStackInSlot(i);

                        if (itemStackInOutputSlot != null && outputItemStackArray[i] != null)
                        {
                            if (!itemStackInOutputSlot.isItemEqual(outputItemStackArray[i]))
	                        {
	                            // DEBUG
	                            System.out.println("Item damage value doesn't match: output slot has damage value = "+itemStackInOutputSlot.getItemDamage()+", item output from recipe has damage value = "+outputItemStackArray[i].getItemDamage());
	                            if(!playerInventory.addItemStackToInventory(itemStackInOutputSlot))
	                            {
	                                EntityItem entityItem = playerInventory.player.entityDropItem(itemStackInOutputSlot, 0.5f);
	                                entityItem.posX = playerInventory.player.posX;
	                                entityItem.posY = playerInventory.player.posY;
	                                entityItem.posZ = playerInventory.player.posZ;
	                            }
	                            outputInventory.setInventorySlotContents(i, null);
	                        }
                        }
                    }
                }

                for(int i = 0; i < outputItemStackArray.length; i++ )
                {
                    ItemStack outputItemStack = outputItemStackArray[i];
                    ItemStack currentStack = outputInventory.getStackInSlot(i);
                    if (outputItemStack != null)
                    {
                        int metadata = outputItemStack.getItemDamage();
                        if(metadata == 32767)
                        {
                            metadata = 0;
                        }
                        ItemStack newStack = null;
                        if(currentStack != null && 1 + currentStack.getCount() <= outputItemStack.getMaxStackSize())
                        {
                            newStack = new ItemStack(outputItemStack.getItem(), 1 + currentStack.getCount(), metadata);
                        }
                        else
                        {
                            if(currentStack != null && !playerInventory.addItemStackToInventory(currentStack))
                            {
                                EntityItem entityItem = playerInventory.player.entityDropItem(currentStack, 0.5f);
                                entityItem.posX = playerInventory.player.posX;
                                entityItem.posY = playerInventory.player.posY;
                                entityItem.posZ = playerInventory.player.posZ;
                            }
                            newStack = new ItemStack(outputItemStack.getItem(), 1, metadata);
                        }
                        outputInventory.setInventorySlotContents(i, newStack);
                    }
                }

                playerInventory.player.addStat(BlockSmith.deconstructedItemsStat, amountRequired);
//                playerInventory.player.triggerAchievement(BlockSmith.deconstructAny);
                
                inputInventory.decrStackSize(0, amountRequired);
            }
        }
        else
        {
            resultString = I18n.format("deconstructing.result.impossible");
            deconstructingState = State.ERROR;
        }
    }

    @Override
	public ItemStack slotClick(int parSlotId, int parMouseButtonId, ClickType parClickMode, EntityPlayer parPlayer)
    {
        ItemStack clickItemStack = super.slotClick(parSlotId, parMouseButtonId, parClickMode, parPlayer);
        if(inventorySlots.size() > parSlotId && parSlotId >= 0)
        {
            if(inventorySlots.get(parSlotId) != null)
            {
            	if(inventorySlots.get(parSlotId).inventory == inputInventory)
                {
                    onCraftMatrixChanged(inputInventory);
                }
            }
        }
        return clickItemStack;
    }

    /**
     * Callback for when the crafting gui is closed.
     */
    @Override
	public void onContainerClosed(EntityPlayer parPlayer)
    {
        if(playerInventory.getItemStack() != null)
        {
            parPlayer.entityDropItem(playerInventory.getItemStack(), 0.5f);
        }
        if(!worldObj.isRemote)
        {
            ItemStack itemStack = inputInventory.removeStackFromSlot(0);
            if(itemStack != null)
            {
                parPlayer.entityDropItem(itemStack, 0.5f);
            }

            for(int i = 0; i < outputInventory.getSizeInventory(); i++ )
            {
                itemStack = outputInventory.removeStackFromSlot(i);

                if(itemStack != null)
                {
                    parPlayer.entityDropItem(itemStack, 0.5f);
                }
            }
        }
    }

    @Override
	public boolean canInteractWith(EntityPlayer player)
    {
        return true;
    }

    /**
     * Called when a player shift-clicks on a slot.
     */
    @Override
	public ItemStack transferStackInSlot(EntityPlayer parPlayer, int parSlotIndex)
    {
//    	// DEBUG
//    	System.out.println("Shift-clicked on a slot");
        Slot slot = inventorySlots.get(parSlotIndex);
        // If there is something in the stack to pick up
        if (slot != null && slot.getHasStack())
        {
        	// If the slot is one of the custom slots
        	if (slot.inventory.equals(inputInventory) || slot.inventory.equals(outputInventory))
            {
        		// try to move to player inventory
                if (!playerInventory.addItemStackToInventory(slot.getStack()))
                {
                    return null;
                }
                slot.putStack(null);
                slot.onSlotChanged();
            }
         	// if the slot is a player inventory slot
            else if(slot.inventory.equals(playerInventory))
            {
            	// DEBUG
            	System.out.println("Shift-clicked on player inventory slot");
            	// Try to transfer to input slot
            	if (!inventorySlots.get(inputSlotNumber).getHasStack())
            	{
            		inventorySlots.get(inputSlotNumber).putStack(slot.getStack());
                    slot.putStack(null);
                    slot.onSlotChanged();
            	}
            	else
            	{
            		// DEBUG
            		System.out.println("There is already something in the input slot");
            	}
            }
        }
        return null;
    }

    @Override
	public boolean canMergeSlot(ItemStack parItemStack, Slot parSlot)
    {
        return !parSlot.inventory.equals(outputInventory);
    }

    @Override
	public Slot getSlot(int parSlotIndex)
    {
        if(parSlotIndex >= inventorySlots.size())
            parSlotIndex = inventorySlots.size() - 1;
        return super.getSlot(parSlotIndex);
    }

}
