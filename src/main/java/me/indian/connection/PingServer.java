package me.indian.connection;


import com.nukkitx.protocol.bedrock.BedrockClient;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


public class PingServer {

    private final InetSocketAddress clientAddress;
    private final InetSocketAddress pingAddress;
    private BedrockClient client;
    private final boolean clientCreated;
    private final boolean clientConnected;


    public PingServer(final InetSocketAddress clientAddress, final InetSocketAddress address) {
        this.pingAddress = address;
        this.clientAddress = clientAddress;
        this.clientCreated = this.tryToCreateClient();
        this.clientConnected = this.tryToConnect();
    }

    private boolean tryToCreateClient() {
        try {
            client = new BedrockClient(clientAddress);
            return true;
        } catch (final Exception e) {
            System.out.println("Nie można utworzyć clienta");
            return false;
        }
    }

    private boolean tryToConnect() {
        System.out.println("Próba połączenia z " + pingAddress);

        try {
            client.bind().join();
            return true;
        } catch (final Exception e) {
            System.out.println("Nie można połączyć clienta");
            return false;
        }
    }

    public int getPlayers() {
        AtomicInteger playerCount = new AtomicInteger();
        if (!this.isClientConnected()) return 0;
        this.client.ping(this.pingAddress).whenComplete((pong, throwable) -> {
            if (throwable != null) {
                return;
            }
            playerCount.set(pong.getPlayerCount());
        }).join();
        return playerCount.get();
    }

    public int getMaxPlayers() {
        AtomicInteger playerMaxCount = new AtomicInteger();
        if (!this.isClientConnected()) return 0;
        this.client.ping(this.pingAddress).whenComplete((pong, throwable) -> {
            if (throwable != null) {
                return;
            }
            playerMaxCount.set(pong.getMaximumPlayerCount());
        }).join();
        return playerMaxCount.get();
    }

    public String getGameType() {
        AtomicReference<String> gameType = new AtomicReference<>("");
        if (!this.isClientConnected()) return "";
        this.client.ping(this.pingAddress).whenComplete((pong, throwable) -> {
            if (throwable != null) {
                return;
            }
            gameType.set(pong.getGameType());
        }).join();
        return gameType.get();
    }

    public int getProtocolVersion() {
        AtomicInteger protocol = new AtomicInteger();
        if (!this.isClientConnected()) return 0;
        this.client.ping(this.pingAddress).whenComplete((pong, throwable) -> {
            if (throwable != null) {
                return;
            }
            protocol.set(pong.getProtocolVersion());
        }).join();
        return protocol.get();
    }

    public void disConnect() {
        System.out.println("Próba rozłączenia...");
        client.close(true);
        System.out.println("Rozłączono");
        exit();
    }

    public boolean isClientCreated() {
        return this.clientCreated;
    }

    public boolean isClientConnected() {
        return this.clientConnected;
    }

    private void exit() {
        System.exit(1);
    }
}
