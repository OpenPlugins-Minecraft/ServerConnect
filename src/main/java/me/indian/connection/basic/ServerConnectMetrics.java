package me.indian.connection.basic;

import cn.nukkit.Server;
import cn.nukkit.plugin.PluginLogger;
import me.indian.connection.ServerConnectNukkit;
import me.indian.connection.ping.PingServer;
import me.indian.connection.util.MessageUtil;

public class ServerConnectMetrics {

    private final Server server;
    private final PluginLogger logger;
    private final Metrics metrics;
    public boolean enabled;

    public ServerConnectMetrics(final ServerConnectNukkit plugin) {
        this.server = plugin.getServer();
        this.logger = plugin.getLogger();
        this.metrics = new Metrics(plugin);
        this.enabled = this.metrics.isEnabled();
    }

    public void run() {
        new Thread(() -> {
            try {
                if (!this.enabled) {
                    this.logger.info(MessageUtil.colorize("&aMetrics is disabled"));
                    Thread.currentThread().interrupt();
                    return;
                }
                this.customMetrics();
                this.logger.info(MessageUtil.colorize("&aLoaded Metrics"));
            } catch (final Exception e) {
                this.logger.info(MessageUtil.colorize("&cCan't load metrics"));
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    private void customMetrics() {
        this.metrics.addCustomChart(new Metrics.SimplePie("nukkit_version", () -> server.getNukkitVersion() + " (MC: " + server.getVersion() + " Nukkit API: " + server.getApiVersion() + ")"));
        this.metrics.addCustomChart(new Metrics.SimplePie("instances_count", () -> String.valueOf(PingServer.getInstanceCount())));
    }
}
