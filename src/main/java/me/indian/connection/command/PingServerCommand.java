package me.indian.connection.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import me.indian.connection.ServerConnectNukkit;
import me.indian.connection.ping.PingServer;
import me.indian.connection.util.MessageUtil;

import java.net.InetSocketAddress;

public class PingServerCommand extends Command {


    private final ServerConnectNukkit plugin;
    private final InetSocketAddress client;

    public PingServerCommand(final ServerConnectNukkit plugin) {
        super("pingserver", "Ping server command");
        commandParameters.clear();

        commandParameters.put("count", new CommandParameter[]{
                CommandParameter.newEnum("count", new CommandEnum("count", "count")),
        });
        commandParameters.put("instances", new CommandParameter[]{
                CommandParameter.newEnum("instances", new CommandEnum("instances", "instances")),
        });
        commandParameters.put("disconnect", new CommandParameter[]{
                CommandParameter.newEnum("disconnect", new CommandEnum("disconnect", "disconnect")),
        });

        commandParameters.put("info", new CommandParameter[]{
                CommandParameter.newEnum("info", new CommandEnum("info", "info")),
                CommandParameter.newType("ip", CommandParamType.STRING),
                CommandParameter.newType("port", CommandParamType.INT)
        });

        this.plugin = plugin;
        final String clientIp = this.plugin.getConfig().getString("BedrockClient.ip", "0.0.0.0");
        final int clientPort = this.plugin.getConfig().getInt("BedrockClient.port", 19130);
        this.client = new InetSocketAddress(clientIp, clientPort);
    }

    @Override
    public boolean execute(final CommandSender sender, final String s, final String[] args) {
        if (!sender.isOp()) return true;

        if (args.length == 0) {
            sender.sendMessage(MessageUtil.colorize("&cBad usages"));
            return false;
        }

        if (args[0].equalsIgnoreCase("count")) {
            sender.sendMessage(MessageUtil.colorize("&aLunched &b" + PingServer.getInstanceCount() + "&a instances"));
            return false;
        }

        if (args[0].equalsIgnoreCase("instances")) {
            if (PingServer.getInstanceCount() == 0) {
                sender.sendMessage(MessageUtil.colorize("&cThere are no active instances"));
                return true;
            }
            for (final PingServer ping : PingServer.getInstances()) {
                sender.sendMessage(ping.getPrefix() + " " + ping.getPingAddress());
            }
            return true;
        }


        if (args[0].equalsIgnoreCase("disconnect")) {
            if (PingServer.getInstanceCount() == 0) {
                sender.sendMessage(MessageUtil.colorize("&cThere are no active instances"));
                return true;
            }
            PingServer.disconnectAllInstances();
            return true;
        }


        if (args[0].equalsIgnoreCase("info")) {
            try {
                String ip = args[1];
                int port;

                if (ip.isEmpty()) {
                    sender.sendMessage(MessageUtil.colorize("&cIp cannot be empty!"));
                    return true;
                }

                try {
                    port = Integer.parseInt(args[2]);
                } catch (final NumberFormatException ex) {
                    sender.sendMessage(MessageUtil.colorize("&cPort must be an integer"));
                    return true;
                }

                final PingServer ping = new PingServer("Command instance ", client, new InetSocketAddress(ip, port), this.plugin.getNukkitLogger());

                ping.tryToConnect();

                sender.sendMessage("Created: " + ping.isClientCreated());
                sender.sendMessage("Connected: " + ping.isClientConnected());
                sender.sendMessage("Gam type: " + ping.getGameType());
                sender.sendMessage("Protocol version: " + ping.getProtocolVersion());
                sender.sendMessage("Player count: " + ping.getPlayers());
                sender.sendMessage("Max players: " + ping.getMaxPlayers());
                ping.disconnect();
                sender.sendMessage("Disconnected");
            } catch (final ArrayIndexOutOfBoundsException exception) {
            sender.sendMessage(MessageUtil.colorize("&cBad usage"));
            }
            return false;
        }

        return true;
    }

    public InetSocketAddress getClient() {
        return this.client;
    }
}
