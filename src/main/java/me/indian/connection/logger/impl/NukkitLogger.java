package me.indian.connection.logger.impl;

import cn.nukkit.plugin.PluginLogger;
import me.indian.connection.logger.ConnectorLogger;

public class NukkitLogger implements ConnectorLogger {

    private final PluginLogger logger;

    public NukkitLogger(final PluginLogger logger) {
        this.logger = logger;
    }

    @Override
    public void emergency(final String log) {
        this.logger.emergency(log);
    }

    @Override
    public void alert(final String log) {
        this.logger.alert(log);
    }

    @Override
    public void critical(final String log) {
        this.logger.critical(log);
    }

    @Override
    public void error(final String log) {
        this.logger.error(log);
    }

    @Override
    public void warning(final String log) {
        this.logger.warning(log);
    }

    @Override
    public void notice(final String log) {
        this.logger.notice(log);
    }

    @Override
    public void info(final String log) {
        this.logger.info(log);
    }
}
