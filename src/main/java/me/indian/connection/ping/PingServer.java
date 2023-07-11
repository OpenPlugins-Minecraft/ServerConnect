package me.indian.connection.ping;


import com.nukkitx.protocol.bedrock.BedrockClient;
import me.indian.connection.logger.ConnectorLogger;
import me.indian.connection.util.MessageUtil;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class PingServer {


    private static int instanceCount = 0;
    private final String prefix;
    private static final List<PingServer> instances = new ArrayList<>();
    private static ConnectorLogger logger;
    private final InetSocketAddress clientAddress;
    private final InetSocketAddress pingAddress;
    private BedrockClient client;
    private final boolean clientCreated;
    private boolean clientConnected = false;


    public PingServer(final String prefix, final InetSocketAddress clientAddress, final InetSocketAddress address, final ConnectorLogger logger) {
        PingServer.logger = logger;
        this.prefix = prefix;
        this.pingAddress = address;
        this.clientAddress = clientAddress;
        this.clientCreated = this.tryToCreateClient();
    }


    private boolean tryToCreateClient() {
        try {
            this.client = new BedrockClient(clientAddress);
            instances.add(this);
            instanceCount++;
            return true;
        } catch (final Exception e) {
            logger.emergency(this.getPrefix() + "Nie można utworzyć clienta");
            return false;
        }
    }

    public void tryToConnect() {
        logger.info(this.getPrefix() + "Próba połączenia z " + pingAddress);
        try {
            this.disconnect();
            client.bind().join();
            logger.info(this.getPrefix() + "Połączono z " + pingAddress);
            clientConnected = true;
        } catch (final Exception e) {
            logger.emergency(this.getPrefix() + "Nie można połączyć clienta");
            e.printStackTrace();
            clientConnected = false;
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
        if (!this.isClientConnected()) return "Not connected";
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

    public boolean isClientCreated() {
        return this.clientCreated;
    }

    public boolean isClientConnected() {
        return this.clientConnected;
    }

    public void disconnect() {
        if (!this.isClientConnected()) return;
        logger.info(this.getPrefix() + "Próba rozłączenia z " + pingAddress + " ....");
        if (client.getSession() != null) {
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

    public BedrockClient getClient() {
        return this.client;
    }

    public InetSocketAddress getClientAddress() {
        return this.clientAddress;
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

    public PingServer reconnect(){
        boolean client = this.isClientCreated();
        if(client){
            boolean connected = this.isClientConnected();
            if(connected){
                this.disconnect();
                this.tryToConnect();
            } else {
                final InetSocketAddress newClient = new InetSocketAddress(this.getClientAddress().getAddress(), this.getClientAddress().getPort() - 10);

               PingServer newPingServer =  new PingServer("NEW "+ this.getPrefix(),newClient , this.getPingAddress(), logger );
                newPingServer.tryToConnect();
               if(newPingServer.clientConnected){
                 return newPingServer;
               }
            }
        } else {
            this.tryToCreateClient();
            this.tryToConnect();
        }
        return null;
    }

    public static void disconnectAllInstances(){
        final List<PingServer> instanceList = getInstances();
        if (instanceList != null) {
            for (final PingServer ping : instanceList) {
                if (ping.isClientConnected()) {
                    logger.info(MessageUtil.colorize("&aDisconnecting: &r" + ping.getPrefix() + ping.getPingAddress()));
                    if (!ping.isClientConnected()) continue;
                    logger.info(ping.getPrefix() + "Próba rozłączenia z " + ping.getPingAddress() + " ....");
                    if (ping.getClient().getSession() != null) {
                        ping.getClient().getSession().disconnect();
                        logger.info(ping.getPrefix() + "Zamknieto sesie");
                    } else {
                        ping.getClient().close(true);
                    }
                    logger.info(ping.getPrefix() + "Rozłączono");
                    --instanceCount;
                    instances.remove(ping);
                    logger.info(MessageUtil.colorize("&aDisconnected"));
                }
            }
        }
    }

    public static void connectAllNoConnected() {
        final List<PingServer> instanceList = getInstances();
        if (instanceList != null) {
            for (final PingServer ping : instanceList) {
                if (!ping.isClientConnected()) {
                    ping.tryToConnect();
                }
            }
        }
    }

    public String getPrefix() {
        return this.prefix + " ";
    }
}
