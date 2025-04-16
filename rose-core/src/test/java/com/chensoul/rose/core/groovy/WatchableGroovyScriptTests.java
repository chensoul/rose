package com.chensoul.rose.core.groovy;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.FileCopyUtils;

@Tag("Groovy")
class WatchableGroovyScriptTests {

    @Test
    void verifyOperation() throws Throwable {
        File file = File.createTempFile("file", ".groovy");
        FileCopyUtils.copy("println 'hello'", new FileWriter(file));
        try (WatchableGroovyScript watchableGroovyScript = new WatchableGroovyScript(new FileSystemResource(file))) {
            assertDoesNotThrow(() -> watchableGroovyScript.execute(ArrayUtils.EMPTY_OBJECT_ARRAY));
        }
        Files.setLastModifiedTime(file.toPath(), FileTime.from(Instant.now()));
        Thread.sleep(5_000);
    }
}
