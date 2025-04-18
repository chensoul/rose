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
