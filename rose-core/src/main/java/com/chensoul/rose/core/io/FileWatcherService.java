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
