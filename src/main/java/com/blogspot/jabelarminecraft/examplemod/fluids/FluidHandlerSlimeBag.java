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
package com.blogspot.jabelarminecraft.examplemod.fluids;

import com.blogspot.jabelarminecraft.examplemod.init.ModFluids;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

// TODO: Auto-generated Javadoc
public class FluidHandlerSlimeBag extends FluidHandlerItemStack
{
	protected static final FluidStack EMPTY = new FluidStack(ModFluids.SLIME, 0); 
	
	/**
	 * Instantiates a new, empty fluid handler slime bag.
	 *
	 * @param parContainerStack the container stack
	 * @param parCapacity the capacity
	 */
	public FluidHandlerSlimeBag(ItemStack parContainerStack, int parCapacity) 
	{
		super(parContainerStack, parCapacity);
		setFluid(EMPTY); // start empty

//		// DEBUG
//		System.out.println("Constructing FluidHandlerSlimeBag with FluidStack = "+getFluid()+" capacity = "+capacity+" and container = "+container);
	}
	
    /* (non-Javadoc)
     * @see net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack#setContainerToEmpty()
     */
    @Override
	protected void setContainerToEmpty()
    {
    	setFluid(EMPTY);
        container.getTagCompound().removeTag(FLUID_NBT_KEY);
    }

    /* (non-Javadoc)
     * @see net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack#canFillFluidType(net.minecraftforge.fluids.FluidStack)
     */
    @Override
	public boolean canFillFluidType(FluidStack fluid)
    {
        return (fluid.getFluid() == ModFluids.SLIME);
    }
    
    public FluidStack getFluidStack()
    {
    	return getFluid();
    }
    
    @Override
    public FluidStack drain(FluidStack sourceFluidStack, boolean doDrain)
    {
    	// check if source is already empty
        if (sourceFluidStack == null)
        {
            return null;
        }
        
        // check that fluid types match
        if (!sourceFluidStack.isFluidEqual(getFluidStack())) 
        {
        	return null;
        }
        
        return drain(sourceFluidStack.amount, doDrain);
    }

    @Override
    public FluidStack drain(int sourceMaxAmount, boolean doDrain)
    { 
    	// would be interesting to allow multiple count
    	// but keep it simple for now.
        if (container.getCount() != 1)
        {
            return null;
        }
        
        // check that source isn't already empty
        if (sourceMaxAmount <= 0)
        {
        	return null;
        }

        // get destination fluid stack
        FluidStack sourceFluidStack = getFluidStack();
        
        // check if destination fluid stack is malformed
        if (sourceFluidStack == null || sourceFluidStack.amount <= 0 )
        {
            return null;
        }
        
        // check if fluid type is possible to drain
        if (!canDrainFluidType(sourceFluidStack))
		{
        	return null;
		}

        final int drainAmount = Math.min(sourceFluidStack.amount, sourceMaxAmount);

        FluidStack drained = sourceFluidStack.copy();
        drained.amount = drainAmount;

        if (doDrain)
        {
            sourceFluidStack.amount -= drainAmount;
            if (sourceFluidStack.amount == 0)
            {
                setContainerToEmpty();
            }
            else
            {
                setFluid(sourceFluidStack);
            }
        }

        return drained;
    }

}
