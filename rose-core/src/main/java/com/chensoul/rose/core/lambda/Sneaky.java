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

import com.chensoul.rose.core.lambda.function.*;
import java.util.Comparator;
import java.util.concurrent.Callable;
import java.util.function.*;

/**
 * Improved interoperability between checked exceptions and Java 8.
 * <p>
 * Similar to {@link Unchecked}, except that {@link Unchecked#RETHROW_ALL} is used as the
 * default way to re-throw checked exceptions.
 *
 * @author Lukas Eder
 */
public final class Sneaky {

    private Sneaky() {}

    /**
     * Wrap a {@link CheckedRunnable} in a {@link Runnable}.
     * <p>
     * Example: <pre><code>
     * new Thread(Unchecked.runnable(() -> {
     *     throw new Exception("Cannot run this thread");
     * })).start();
     * </code></pre>
     */
    public static Runnable runnable(CheckedRunnable runnable) {
        return Unchecked.runnable(runnable, Unchecked.RETHROW_ALL);
    }

    /**
     * Wrap a {@link CheckedCallable} in a {@link Callable}.
     * <p>
     * Example: <pre><code>
     * Executors.newFixedThreadPool(1).submit(Unchecked.callable(() -> {
     *     throw new Exception("Cannot execute this task");
     * })).get();
     * </code></pre>
     */
    public static <T> Callable<T> callable(CheckedCallable<T> callable) {
        return Unchecked.callable(callable, Unchecked.RETHROW_ALL);
    }

    /**
     * Wrap a {@link CheckedComparator} in a {@link Comparator}.
     */
    public static <T> Comparator<T> comparator(CheckedComparator<T> comparator) {
        return Unchecked.comparator(comparator, Unchecked.RETHROW_ALL);
    }

    /**
     * Wrap a {@link CheckedBiConsumer} in a {@link BiConsumer}.
     * <p>
     * Example: <pre><code>
     * map.forEach(Unchecked.biConsumer((k, v) -> {
     *     if (k == null || v == null)
     *         throw new Exception("No nulls allowed in map");
     * }));
     * </code></pre>
     */
    public static <T, U> BiConsumer<T, U> biConsumer(CheckedBiConsumer<T, U> consumer) {
        return Unchecked.biConsumer(consumer, Unchecked.RETHROW_ALL);
    }

    /**
     * Wrap a {@link CheckedBiFunction} in a {@link BiFunction}.
     * <p>
     * Example: <pre><code>
     * map.computeIfPresent("key", Unchecked.biFunction((k, v) -> {
     *     if (k == null || v == null)
     *         throw new Exception("No nulls allowed in map");
     *
     *     return 42;
     * }));
     * </code></pre>
     */
    public static <T, U, R> BiFunction<T, U, R> biFunction(CheckedBiFunction<T, U, R> function) {
        return Unchecked.biFunction(function, Unchecked.RETHROW_ALL);
    }

    /**
     * Wrap a {@link CheckedBiPredicate} in a {@link BiPredicate}.
     */
    public static <T, U> BiPredicate<T, U> biPredicate(CheckedBiPredicate<T, U> predicate) {
        return Unchecked.biPredicate(predicate, Unchecked.RETHROW_ALL);
    }

    /**
     * Wrap a {@link CheckedConsumer} in a {@link Consumer}.
     * <p>
     * Example: <pre><code>
     * Arrays.asList("a", "b").stream().forEach(Unchecked.consumer(s -> {
     *     if (s.length() > 10)
     *         throw new Exception("Only short strings allowed");
     * }));
     * </code></pre>
     */
    public static <T> Consumer<T> consumer(CheckedConsumer<T> consumer) {
        return Unchecked.consumer(consumer, Unchecked.RETHROW_ALL);
    }

    /**
     * Wrap a {@link CheckedFunction} in a {@link Function}.
     * <p>
     * Example: <pre><code>
     * map.computeIfAbsent("key", Unchecked.function(k -> {
     *     if (k.length() > 10)
     *         throw new Exception("Only short strings allowed");
     *
     *     return 42;
     * }));
     * </code></pre>
     */
    public static <T, R> Function<T, R> function(CheckedFunction<T, R> function) {
        return Unchecked.function(function, Unchecked.RETHROW_ALL);
    }

    /**
     * Wrap a {@link CheckedPredicate} in a {@link Predicate}.
     * <p>
     * Example: <pre><code>
     * Stream.of("a", "b", "c").filter(Unchecked.predicate(s -> {
     *     if (s.length() > 10)
     *         throw new Exception("Only short strings allowed");
     *
     *     return true;
     * }));
     * </code></pre>
     */
    public static <T> Predicate<T> predicate(CheckedPredicate<T> predicate) {
        return Unchecked.predicate(predicate, Unchecked.RETHROW_ALL);
    }

    /**
     * Wrap a {@link CheckedSupplier} in a {@link Supplier}.
     * <p>
     * Example: <pre><code>
     * ResultSet rs = statement.executeQuery();
     * Stream.generate(Unchecked.supplier(() -> rs.getObject(1)));
     * </code></pre>
     */
    public static <T> Supplier<T> supplier(CheckedSupplier<T> supplier) {
        return Unchecked.supplier(supplier, Unchecked.RETHROW_ALL);
    }
}
