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

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.blogspot.jabelarminecraft.blocksmith.BlockSmith;

/**
 * @author jabelar
 *
 */
public class MessageSendItemStackRegistryToServer implements IMessage 
{   
    public MessageSendItemStackRegistryToServer() 
    {
        // need this constructor
        
        // DEBUG
        System.out.println("Constructor");
    }

    @Override
    public void toBytes(ByteBuf parBuffer) 
    {
        // DEBUG
        System.out.println("toBytes encoded");
        BlockSmith.proxy.convertItemStackListToPayload(parBuffer); // appends directly to the buffer passed in
    }

    @Override
    public void fromBytes(ByteBuf parBuffer) 
    {
        // DEBUG
        System.out.println("fromBytes");
        BlockSmith.proxy.setItemStackRegistry(BlockSmith.proxy.convertPayloadToItemStackList(parBuffer)); 
    }

    public static class Handler implements IMessageHandler<MessageSendItemStackRegistryToServer, IMessage> 
    {
        @Override
        public IMessage onMessage(final MessageSendItemStackRegistryToServer message, MessageContext ctx) 
        {
            // DEBUG
            System.out.println("Message received");
            // Know it will be on the server so make it thread-safe
            final EntityPlayerMP thePlayer = (EntityPlayerMP) BlockSmith.proxy.getPlayerEntityFromContext(ctx);
            thePlayer.getServer().addScheduledTask(
                    new Runnable()
                    {
                        @Override
                        public void run() 
                        {
                            // don't need to do anything because the fromBytes operates directly 
                            // on public field in main class
                        }
                    }
                    );
            return null; // no response message
        }
    }
}
