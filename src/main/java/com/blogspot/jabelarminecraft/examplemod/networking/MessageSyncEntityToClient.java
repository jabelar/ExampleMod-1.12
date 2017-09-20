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

package com.blogspot.jabelarminecraft.examplemod.networking;

import com.blogspot.jabelarminecraft.examplemod.MainMod;
import com.blogspot.jabelarminecraft.examplemod.entities.IEntity;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

// TODO: Auto-generated Javadoc
/**
 * @author jabelar
 *
 */
public class MessageSyncEntityToClient implements IMessage 
{
    private int entityId ;
    private NBTTagCompound entitySyncDataCompound;

    /**
     * Instantiates a new message sync entity to client.
     */
    public MessageSyncEntityToClient() 
    { 
    	// need this constructor
    }

    /**
     * Instantiates a new message sync entity to client.
     *
     * @param parEntityId the par entity id
     * @param parTagCompound the par tag compound
     */
    public MessageSyncEntityToClient(int parEntityId, NBTTagCompound parTagCompound) 
    {
    	entityId = parEntityId;
        entitySyncDataCompound = parTagCompound;
        // DEBUG
        System.out.println("SyncEntityToClient constructor");
    }

    /* (non-Javadoc)
     * @see net.minecraftforge.fml.common.network.simpleimpl.IMessage#fromBytes(io.netty.buffer.ByteBuf)
     */
    @Override
    public void fromBytes(ByteBuf buf) 
    {
    	entityId = ByteBufUtils.readVarInt(buf, 4);
    	entitySyncDataCompound = ByteBufUtils.readTag(buf); // this class is very useful in general for writing more complex objects
    	// DEBUG
    	System.out.println("fromBytes");
    }

    /* (non-Javadoc)
     * @see net.minecraftforge.fml.common.network.simpleimpl.IMessage#toBytes(io.netty.buffer.ByteBuf)
     */
    @Override
    public void toBytes(ByteBuf buf) 
    {
    	ByteBufUtils.writeVarInt(buf, entityId, 4);
    	ByteBufUtils.writeTag(buf, entitySyncDataCompound);
        // DEBUG
        System.out.println("toBytes encoded");
    }

    public static class Handler implements IMessageHandler<MessageSyncEntityToClient, IMessage> 
    {
        
        /* (non-Javadoc)
         * @see net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler#onMessage(net.minecraftforge.fml.common.network.simpleimpl.IMessage, net.minecraftforge.fml.common.network.simpleimpl.MessageContext)
         */
        @Override
        public IMessage onMessage(MessageSyncEntityToClient message, MessageContext ctx) 
        {
        	EntityPlayer thePlayer = MainMod.proxy.getPlayerEntityFromContext(ctx);
        	IEntity theEntity = (IEntity)thePlayer.world.getEntityByID(message.entityId);
        	// DEBUG
        	if (theEntity == null)
        	{
        		System.out.println("The entity is null for entity ID ="+message.entityId);
        	}
        	else
        	{
        		System.out.println("MessageSyncEnitityToClient onMessage(), entity ID = "+message.entityId);
        	}
        	theEntity.setSyncDataCompound(message.entitySyncDataCompound);
            return null; // no response in this case
        }
    }
}
