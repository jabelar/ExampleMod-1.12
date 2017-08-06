/**
    Copyright (C) 2014 by jabelar

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

package com.blogspot.jabelarminecraft.blocksmith.networking;

import com.blogspot.jabelarminecraft.blocksmith.MainMod;
import com.blogspot.jabelarminecraft.blocksmith.entities.IEntity;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author jabelar
 *
 */
public class MessageSyncEntityToServer implements IMessage 
{
    private int entityId ;
    private NBTTagCompound entitySyncDataCompound;

    public MessageSyncEntityToServer() 
    { 
    	// need this constructor
    }

    public MessageSyncEntityToServer(int parEntityId, NBTTagCompound parTagCompound) 
    {
    	entityId = parEntityId;
        entitySyncDataCompound = parTagCompound;
        // DEBUG
        System.out.println("SyncEntityToClient constructor");
    }

    @Override
    public void fromBytes(ByteBuf buf) 
    {
    	entityId = ByteBufUtils.readVarInt(buf, 4);
    	entitySyncDataCompound = ByteBufUtils.readTag(buf); // this class is very useful in general for writing more complex objects
    	// DEBUG
    	System.out.println("fromBytes");
    }

    @Override
    public void toBytes(ByteBuf buf) 
    {
    	ByteBufUtils.writeVarInt(buf, entityId, 4);
    	ByteBufUtils.writeTag(buf, entitySyncDataCompound);
        // DEBUG
        System.out.println("toBytes encoded");
    }

    public static class Handler implements IMessageHandler<MessageSyncEntityToServer, IMessage> 
    {
        @Override
        public IMessage onMessage(final MessageSyncEntityToServer message, MessageContext ctx) 
        {
            // Know it will be on the server so make it thread-safe
            final EntityPlayerMP thePlayer = (EntityPlayerMP) MainMod.proxy.getPlayerEntityFromContext(ctx);
            thePlayer.getServer().addScheduledTask(
                    new Runnable()
                    {
                        @Override
                        public void run() 
                        {
                            IEntity theEntity = (IEntity)thePlayer.world.getEntityByID(message.entityId);
                            theEntity.setSyncDataCompound(message.entitySyncDataCompound);
                            // DEBUG
                            System.out.println("MessageSyncEnitityToClient onMessage(), entity ID = "+message.entityId);
                            return; 
                        }
                    }
            );
            return null; // no response in this case
        }
    }
}
