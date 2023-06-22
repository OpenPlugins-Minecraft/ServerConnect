package me.indian.connection.logger.impl;

import me.indian.connection.logger.ConnectorLogger;

public class BasicLogger implements ConnectorLogger {

    //advanced loger system will be added soon

    @Override
    public void emergency(final String log) {
        System.out.println(log);
    }

    @Override
    public void alert(final String log) {
        System.out.println(log);
    }

    @Override
    public void critical(final String log) {
        System.out.println(log);
    }

    @Override
    public void error(final String log) {
        System.out.println(log);
    }

    @Override
    public void warning(final String log) {
        System.out.println(log);
    }

    @Override
    public void notice(final String log) {
        System.out.println(log);
    }

    @Override
    public void info(final String log) {
        System.out.println(log);
    }
}
