package me.indian.connection;

import me.indian.connection.logger.impl.BasicLogger;

public class ServerConnectBasic {

    private final BasicLogger logger;


    public ServerConnectBasic() {
        this.logger = new BasicLogger();
    }

    public BasicLogger getBasicLogger() {
        return this.logger;
    }
}
