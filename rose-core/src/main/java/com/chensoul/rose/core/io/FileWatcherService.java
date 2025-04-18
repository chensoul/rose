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

import java.io.File;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
public class FileWatcherService extends PathWatcherService {

    public FileWatcherService(
            final File watchableFile,
            final Consumer<File> onCreate,
            final Consumer<File> onModify,
            final Consumer<File> onDelete) {
        super(
                watchableFile.getParentFile().toPath(),
                getWatchedFileConsumer(watchableFile, onCreate),
                getWatchedFileConsumer(watchableFile, onModify),
                getWatchedFileConsumer(watchableFile, onDelete));
    }

    public FileWatcherService(final File watchableFile, final Consumer<File> onModify) {
        super(watchableFile.getParentFile(), getWatchedFileConsumer(watchableFile, onModify));
    }

    private static Consumer<File> getWatchedFileConsumer(final File watchableFile, final Consumer<File> consumer) {
        return file -> {
            if (file.getPath().equals(watchableFile.getPath())) {
                log.trace("Detected change in file [{}] and calling change consumer to handle event", file);
                consumer.accept(file);
            }
        };
    }
}
