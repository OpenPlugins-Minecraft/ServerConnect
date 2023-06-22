package me.indian.connection;

import cn.nukkit.plugin.PluginBase;

import java.net.InetSocketAddress;

public class ServerConnect extends PluginBase {


    @Override
    public void onEnable() {

        test();

    }


    private void test(){

        final InetSocketAddress address = new InetSocketAddress("play.cubecraft.net", 19132);
        final InetSocketAddress client = new InetSocketAddress("0.0.0.0", 19134);

        final PingServer ping = new PingServer(client, address);


        System.out.println("Created: " + ping.isClientCreated());
        System.out.println("Connected: " + ping.isClientConnected());

        System.out.println("Gam type: " + ping.getGameType());
        System.out.println("Protocol version: " + ping.getProtocolVersion());
        System.out.println("Max players: " + ping.getMaxPlayers());

        for (int i = 0; i < 5; i++) {
            System.out.println("Sprawdzenie graczy nr " + i + " " + ping.getPlayers() + " / " + ping.getMaxPlayers());
//            try {
//                Thread.sleep(2000);
//            } catch (final InterruptedException e) {
//                System.out.println("Wątek został przerwany.");
//            }
        }

    }

    @Override
    public void onDisable() {

    }
}