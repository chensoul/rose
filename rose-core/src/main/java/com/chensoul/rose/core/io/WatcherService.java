/*
 * Copyright © 2025 Chensoul, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
