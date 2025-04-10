package com.chensoul.core.lambda;

import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.concurrent.CompletionStage;
import org.junit.jupiter.api.Test;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since TODO
 */
class AsyncTest {

    @Test
    public void testNoCustomExecutor() {
        CompletionStage<Void> completionStage = Async.runAsync(() -> {});
        assertNull(completionStage.toCompletableFuture().join());

        completionStage = Async.supplyAsync(() -> null);
        assertNull(completionStage.toCompletableFuture().join());
    }
}
