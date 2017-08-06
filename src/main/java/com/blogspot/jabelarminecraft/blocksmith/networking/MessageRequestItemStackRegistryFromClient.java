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
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author jabelar
 *
 */
public class MessageRequestItemStackRegistryFromClient implements IMessage 
{
    public MessageRequestItemStackRegistryFromClient() 
    { 
    	// need this constructor
        // DEBUG
        System.out.println("constructor");
    }

    @Override
    public void fromBytes(ByteBuf buf) 
    {
    	// DEBUG
    	System.out.println("fromBytes");
    }

    @Override
    public void toBytes(ByteBuf buf) 
    {
        // DEBUG
        System.out.println("toBytes encoded");
    }

    public static class Handler implements IMessageHandler<MessageRequestItemStackRegistryFromClient, IMessage> 
    {
        @Override
        public IMessage onMessage(MessageRequestItemStackRegistryFromClient message, MessageContext ctx) 
        {
            // DEBUT
            System.out.println("message received");
            
            return new MessageSendItemStackRegistryToServer(); // no response in this case
        }
    }
}
