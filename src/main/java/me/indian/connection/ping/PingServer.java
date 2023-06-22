package me.indian.connection.ping;


import cn.nukkit.plugin.PluginLogger;
import com.nukkitx.protocol.bedrock.BedrockClient;
import me.indian.connection.ServerConnect;
import me.indian.connection.util.MessageUtil;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class PingServer {


    private static int instanceCount = 0;
    private final String prefix;
    private static final List<PingServer> instances = new ArrayList<>();
    private final ServerConnect plugin = ServerConnect.getInstance();
    private static PluginLogger logger;
    private final InetSocketAddress clientAddress;
    private final InetSocketAddress pingAddress;
    private BedrockClient client;
    private final boolean clientCreated;
    private final boolean clientConnected;


    public PingServer(final String prefix, final InetSocketAddress clientAddress, final InetSocketAddress address) {
        logger = this.plugin.getLogger();
        this.prefix = prefix;
        this.pingAddress = address;
        this.clientAddress = clientAddress;
        this.clientCreated = this.tryToCreateClient();
        this.clientConnected = this.tryToConnect();
        instanceCount++;
        instances.add(this);
    }


    private boolean tryToCreateClient() {
        try {
            client = new BedrockClient(clientAddress);
            return true;
        } catch (final Exception e) {
            logger.emergency(this.getPrefix() + "Nie można utworzyć clienta");
            return false;
        }
    }

    private boolean tryToConnect() {
        logger.info(this.getPrefix() + "Próba połączenia z " + pingAddress);

        try {
            client.bind().join();
            logger.info(this.getPrefix() + "Połączono z " + pingAddress);
            return true;
        } catch (final Exception e) {
            logger.emergency(this.getPrefix() + "Nie można połączyć clienta");
            e.printStackTrace();
            return false;
        }
    }

    public int getPlayers() {
        if (!this.isClientConnected()) return 0;
        try {
            return this.client.ping(this.pingAddress).get().getPlayerCount();
        } catch (InterruptedException | ExecutionException e) {
            return 0;
        }
    }

    public int getMaxPlayers() {
        if (!this.isClientConnected()) return 0;
        try {
            return this.client.ping(this.pingAddress).get().getMaximumPlayerCount();
        } catch (InterruptedException | ExecutionException e) {
            return 0;
        }
    }

    public String getGameType() {
        if (!this.isClientConnected()) return "";
        try {
            return this.client.ping(this.pingAddress).get().getGameType();
        } catch (InterruptedException | ExecutionException e) {
            return "";
        }
    }

    public int getProtocolVersion() {
        if (!this.isClientConnected()) return 0;
        try {
            return this.client.ping(this.pingAddress).get().getProtocolVersion();
        } catch (InterruptedException | ExecutionException e) {
            return 0;
        }
    }

    public void disConnect() {
     logger.info(this.getPrefix() + "Próba rozłączenia z " + pingAddress + " ....");
        if(client.getSession() != null){
            client.getSession().disconnect();
            logger.info(this.getPrefix() + "Zamknieto sesie");
            return;
        } else {
            client.close(true);
        }
        logger.info(this.getPrefix() + "Rozłączono");
        --instanceCount;
        instances.remove(this);
    }

    public boolean isClientCreated() {
        return this.clientCreated;
    }

    public boolean isClientConnected() {
        return this.clientConnected;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public InetSocketAddress getPingAddress() {
        return this.pingAddress;
    }

    public static int getInstanceCount() {
        return instanceCount;
    }

    public static List<PingServer> getInstances() {
        return instances;
    }

    public static void disconnectAllInstances(){
        final List<PingServer> instanceList = getInstances();
        if (instanceList != null) {
            for (final PingServer ping : instanceList) {
                logger.info(MessageUtil.colorize("&aDisconnecting: &r" + ping.getPrefix() + " " + ping.getPingAddress()));
                ping.disConnect();
                logger.info(MessageUtil.colorize("&aDisconnected"));
            }
        }
    }
}
