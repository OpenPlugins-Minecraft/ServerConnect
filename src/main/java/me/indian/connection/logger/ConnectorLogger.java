package me.indian.connection.logger;


public interface ConnectorLogger {


    void emergency(final String log);

    void alert(final String log);

    void critical(final String log);

    void error(final String log);

    void warning(final String log);

    void notice(final String log);

    void info(final String log);

}
