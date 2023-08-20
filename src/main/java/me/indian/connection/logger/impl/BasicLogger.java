package me.indian.connection.logger.impl;

import me.indian.connection.logger.ConnectorLogger;
import me.indian.connection.logger.LogState;
import me.indian.connection.util.ConsoleColors;

public class BasicLogger implements ConnectorLogger {
    private String prefix;
    private LogState logState;

    public BasicLogger() {
        logState = LogState.NONE;
        updatePrefix();
    }

    private void updatePrefix() {
        String logStateColor = logState.getColorCode();
        prefix = ConsoleColors.BRIGHT_GREEN + "PingServer " +
                ConsoleColors.BRIGHT_GREEN + "[" + ConsoleColors.BRIGHT_GRAY +
                Thread.currentThread().getName()
                + ConsoleColors.BRIGHT_GREEN + "]" +
                " " + logStateColor + logState.name().toUpperCase()
                + ConsoleColors.RESET + " ";
    }

    public void alert(String log) {
        logState = LogState.ALERT;
        updatePrefix();
        System.out.println(prefix + log);
    }

    public void critical(String log) {
        logState = LogState.CRITICAL;
        updatePrefix();
        System.out.println(prefix + log);
    }

    public void error(String log) {
        logState = LogState.ERROR;
        updatePrefix();
        System.out.println(prefix + log);
    }

    public void warning(String log) {
        logState = LogState.WARNING;
        updatePrefix();
        System.out.println(prefix + log);
    }

    public void info(String log) {
        logState = LogState.INFO;
        updatePrefix();
        System.out.println(prefix + log);
    }
}
