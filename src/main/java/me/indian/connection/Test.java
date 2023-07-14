package me.indian.connection;


import me.indian.connection.logger.impl.BasicLogger;
import me.indian.connection.ping.PingServer;

import java.net.InetSocketAddress;

public class Test {

    // class for developer tests

    private static final BasicLogger logger = new ServerConnectBasic().getBasicLogger();

    public static void main(String[] args) {
        final InetSocketAddress client = new InetSocketAddress("0.0.0.0", 19112);
        final InetSocketAddress address = new InetSocketAddress("play.skyblockpe.com", 19132);

        final PingServer ping = new PingServer("Test " + PingServer.getInstanceCount(), client, address, logger);
        ping.tryToConnect();


        logger.info("Created: " + ping.isClientCreated());
        logger.info("Connected: " + ping.isClientConnected());
        logger.info("Gam type: " + ping.getGameType());
        logger.info("Protocol version: " + ping.getProtocolVersion());
        logger.info("Player count: " + ping.getPlayers());
        logger.info("Max players: " + ping.getMaxPlayers());

        ping.disconnect();

        check();

    }


    private static void check() {
        if (PingServer.getInstanceCount() == 0) {
            logger.info("There are no active instances");
        }
        for (final PingServer ping : PingServer.getInstances()) {
            logger.info(ping.getPrefix() + " " + ping.getPingAddress() + " " + ping.getPlayers());
        }
    }
}