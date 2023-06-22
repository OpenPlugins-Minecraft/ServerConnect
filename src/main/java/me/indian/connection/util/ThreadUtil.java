package me.indian.connection.util;

import java.util.concurrent.ThreadFactory;

public class ThreadUtil implements ThreadFactory {
    private final String threadName;

    public ThreadUtil(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setName(threadName);
        return thread;
    }
}
