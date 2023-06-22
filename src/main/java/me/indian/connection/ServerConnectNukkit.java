package me.indian.connection;

import cn.nukkit.command.CommandMap;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import me.indian.connection.basic.ServerConnectMetrics;
import me.indian.connection.command.PingServerCommand;
import me.indian.connection.logger.impl.NukkitLogger;
import me.indian.connection.ping.PingServer;
import me.indian.connection.util.MessageUtil;

import java.net.InetSocketAddress;

public class ServerConnectNukkit extends PluginBase {


    private static ServerConnectNukkit instance;
    private PingServerCommand pingServerCommand;
    private NukkitLogger nukkitLogger;

    public static ServerConnectNukkit getInstance() {
        return instance;
    }

    public PingServerCommand getPingServerCommand() {
        return this.pingServerCommand;
    }

    public NukkitLogger getNukkitLogger() {
        return this.nukkitLogger;
    }

    @Override
    public void onLoad() {
        instance = this;
        this.saveDefaultConfig();
        this.pingServerCommand = new PingServerCommand(this);
        this.nukkitLogger = new NukkitLogger(this.getLogger());
    }

    @Override
    public void onEnable() {
        final PluginManager pluginManager = this.getServer().getPluginManager();
        final CommandMap commandMap = this.getServer().getCommandMap();
        commandMap.register("ServerConnect", this.getPingServerCommand());

        if (this.testConnection()) {
            this.getLogger().info(MessageUtil.colorize("&aTested connection , plugin working!"));
        } else {
            this.getLogger().emergency(MessageUtil.colorize("&cCan't test connection!"));
            pluginManager.disablePlugin(this);
            return;
        }

        new ServerConnectMetrics(this).run();
    }

    private boolean testConnection() {
        final String ip = this.getConfig().getString("TestConnection.ip", "play.cubecraft.net");
        final int port = this.getConfig().getInt("TestConnection.port", 19132);
        final InetSocketAddress address = new InetSocketAddress(ip, port);
        final PingServer ping = new PingServer("Connect test ", this.getPingServerCommand().getClient(), address, this.getNukkitLogger());
        ping.tryToConnect();
        ping.disconnect();
        return ping.isClientConnected();
    }

    @Override
    public void onDisable() {
        PingServer.disconnectAllInstances();
    }
}