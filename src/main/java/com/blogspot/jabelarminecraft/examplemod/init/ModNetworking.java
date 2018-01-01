package com.blogspot.jabelarminecraft.examplemod.init;

import com.blogspot.jabelarminecraft.examplemod.MainMod;
import com.blogspot.jabelarminecraft.examplemod.networking.MessageExtendedReachAttack;
import com.blogspot.jabelarminecraft.examplemod.networking.MessageSyncEntityToClient;
import com.blogspot.jabelarminecraft.examplemod.networking.MessageToClient;
import com.blogspot.jabelarminecraft.examplemod.networking.MessageToServer;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ModNetworking
{
    // use a named channel to identify packets related to this mod
    public static final String NETWORK_CHANNEL_NAME = MainMod.MODID;
    public static SimpleNetworkWrapper network;
    /*
     * Thanks to diesieben07 tutorial for this code
     */
    /**
     * Registers the simple networking channel and messages for both sides.
     */
    public static void registerSimpleNetworking()
    {
        // DEBUG
        System.out.println("Registering simple networking");
        network = NetworkRegistry.INSTANCE.newSimpleChannel(NETWORK_CHANNEL_NAME);

        int packetId = 0;
        // register messages from client to server
        network.registerMessage(MessageToServer.Handler.class, MessageToServer.class, packetId++, Side.SERVER);
        // register messages from server to client
        network.registerMessage(MessageToClient.Handler.class, MessageToClient.class, packetId++, Side.CLIENT);
        network.registerMessage(MessageSyncEntityToClient.Handler.class, MessageSyncEntityToClient.class, packetId++, Side.CLIENT);
        network.registerMessage(MessageExtendedReachAttack.Handler.class, MessageExtendedReachAttack.class, packetId++, Side.SERVER);
    }

}
