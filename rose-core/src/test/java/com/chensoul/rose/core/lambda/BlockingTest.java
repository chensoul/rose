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
package com.chensoul.rose.core.lambda;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class BlockingTest {

    private static ForkJoinPool pool = new ForkJoinPool(1);

    /**
     * Test that {@link Blocking#runnable(java.lang.Runnable) } executes the given
     * runnable.
     */
    @Test
    public void testRunnable_run() {
        AtomicBoolean executed = new AtomicBoolean(false);

        Runnable runnable = Blocking.runnable(() -> executed.set(true));

        CompletableFuture.runAsync(runnable, pool).join();
        Assert.assertTrue("The given runnable has not been executed", executed.get());
    }
}
