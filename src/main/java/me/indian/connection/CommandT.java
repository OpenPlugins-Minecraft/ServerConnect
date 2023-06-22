package me.indian.connection;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

import java.net.InetSocketAddress;

public class CommandT extends Command {
    public CommandT(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {

        final InetSocketAddress address = new InetSocketAddress("play.cubecraft.net", 19132);
        final InetSocketAddress client = new InetSocketAddress("0.0.0.0", 19139);

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

        return false;
    }
}
