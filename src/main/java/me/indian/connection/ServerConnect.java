package me.indian.connection;

import cn.nukkit.command.CommandMap;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import me.indian.connection.basic.ServerConnectMetrics;
import me.indian.connection.command.PingServerCommand;
import me.indian.connection.ping.PingServer;
import me.indian.connection.util.MessageUtil;

import java.net.InetSocketAddress;

public class ServerConnect extends PluginBase {


    private static ServerConnect instance;
    private PingServerCommand pingServerCommand;

    public static ServerConnect getInstance() {
        return instance;
    }

    public PingServerCommand getPingServerCommand() {
        return this.pingServerCommand;
    }

    @Override
    public void onLoad() {
        instance = this;
        this.saveDefaultConfig();
        this.pingServerCommand = new PingServerCommand(this);
    }

    @Override
    public void onEnable() {
        final PluginManager pluginManager = this.getServer().getPluginManager();
        final CommandMap commandMap = this.getServer().getCommandMap();
        commandMap.register("ServerConnect", this.getPingServerCommand());

        if (this.testConnect()) {
            this.getLogger().info(MessageUtil.colorize("&aTested connection , plugin working!"));
        } else {
            this.getLogger().emergency(MessageUtil.colorize("&cCan't test connection!"));
            pluginManager.disablePlugin(this);
            return;
        }

        test();
        test2();

        new ServerConnectMetrics().run();
    }


    private boolean testConnect() {
        final String ip = this.getConfig().getString("TestConnection.ip", "play.cubecraft.net");
        final int port = this.getConfig().getInt("TestConnection.port", 19132);
        final InetSocketAddress address = new InetSocketAddress(ip, port);
        final PingServer ping = new PingServer("Connect test ", this.getPingServerCommand().getClient(), address);
        ping.disConnect();
        return ping.isClientConnected();
    }


    private void test2() {

        final InetSocketAddress client1 = new InetSocketAddress("0.0.0.0", 19137);
        final InetSocketAddress client2 = new InetSocketAddress("0.0.0.0", 19112);
        final InetSocketAddress client3 = new InetSocketAddress("0.0.0.0", 19121);

        final InetSocketAddress address = new InetSocketAddress("play.skyblockpe.com", 19132);


        final PingServer ping1 = new PingServer("Test " + PingServer.getInstanceCount(), client1, address);
        final PingServer ping2 = new PingServer("Test " + PingServer.getInstanceCount(), client2, address);
        final PingServer ping3 = new PingServer("Test " + PingServer.getInstanceCount(), client3, address);


        this.getLogger().info(ping1.isClientConnected() + "");
        this.getLogger().alert(PingServer.getInstanceCount() + " Instancij!");

        this.getLogger().info(ping2.isClientConnected() + "");
        this.getLogger().alert(PingServer.getInstanceCount() + " Instancij!");


        this.getLogger().info(ping3.isClientConnected() + "");
        this.getLogger().alert(PingServer.getInstanceCount() + " Instancij!");


        ping3.disConnect();
        this.getLogger().alert(PingServer.getInstanceCount() + " Instancij!");


        for (final PingServer ping : PingServer.getInstances()) {
            this.getLogger().alert(ping.getPrefix() + " " + ping.getPingAddress() + " " + ping.getPlayers());
        }
    }

    private void test() {
        final InetSocketAddress address = new InetSocketAddress("play.cubecraft.net", 19132);
        final PingServer ping = new PingServer("Test " + PingServer.getInstanceCount(), this.getPingServerCommand().getClient(), address);

        System.out.println("Created: " + ping.isClientCreated());
        System.out.println("Connected: " + ping.isClientConnected());

        System.out.println();
        System.out.println("Gam type: " + ping.getGameType());
        System.out.println("Protocol version: " + ping.getProtocolVersion());
        System.out.println("Max players: " + ping.getMaxPlayers());

        for (int i = 0; i < 5; i++) {
            System.out.println("Sprawdzenie graczy nr " + i + " " + ping.getPlayers() + " / " + ping.getMaxPlayers());
        }
        ping.disConnect();
    }


    @Override
    public void onDisable() {
        PingServer.disconnectAllInstances();
    }
}