package com.chensoul.rose.core.util;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since TODO
 */
@Slf4j
class NanoIdUtilsTest {

    @Test
    public void testNanoId() {
        log.info(NanoIdUtils.randomNanoId());
        log.info(NanoIdUtils.randomNanoId(10));
    }

    @Test
    public void testUUID() {
        log.info(UUID.randomUUID().toString());
        log.info("length: {}", UUID.randomUUID().toString().length());
    }
}
