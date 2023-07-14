package me.indian.connection.logger.impl;

import cn.nukkit.plugin.PluginLogger;
import me.indian.connection.logger.ConnectorLogger;

public class NukkitLogger implements ConnectorLogger {

    private final PluginLogger logger;

    public NukkitLogger(final PluginLogger logger) {
        this.logger = logger;
    }


    public void alert(String log) {
        this.logger.info(log);
    }


    public void critical(String log) {
        this.logger.info(log);
    }


    public void error(String log) {
        this.logger.info(log);
    }


    public void warning(String log) {
        this.logger.info(log);
    }


    public void info(String log) {
        this.logger.info(log);
    }

}
