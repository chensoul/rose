package com.chensoul.rose.core.io;

import java.io.Closeable;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public interface WatcherService extends Closeable {

    /**
     * No op watcher util.
     *
     * @return the watcher util
     */
    static WatcherService noOp() {
        return new WatcherService() {};
    }

    /**
     * Close.
     */
    @Override
    default void close() {}

    /**
     * Start the watch.
     *
     * @param name the name
     */
    default void start(final String name) {}
}
