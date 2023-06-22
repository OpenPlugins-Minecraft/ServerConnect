package me.indian.connection.basic;

import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginLogger;
import cn.nukkit.utils.Config;
import me.indian.connection.ServerConnect;
import me.indian.connection.util.ThreadUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.indian.connection.ServerConnect;
import me.indian.connection.ping.PingServer;
import me.indian.connection.util.MessageUtil;

public class ServerConnectMetrics {

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor(new ThreadUtil("ServerConnect Metrics Thread"));
    private final ServerConnect plugin = ServerConnect.getInstance();
    private final Server server = plugin.getServer();
    private final PluginLogger logger = this.plugin.getLogger();
    private final Metrics metrics = new Metrics(this.plugin);
    public boolean enabled = this.metrics.isEnabled();

    public void run() {
        executorService.execute(() -> {
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
        });
    }

    private void customMetrics() {
        this.metrics.addCustomChart(new Metrics.SimplePie("nukkit_version", () -> server.getNukkitVersion() + " (MC: " + server.getVersion() + " Nukkit API: " + server.getApiVersion() + ")"));
        this.metrics.addCustomChart(new Metrics.SimplePie("instances_count", () -> String.valueOf(PingServer.getInstanceCount())));
    }
}
