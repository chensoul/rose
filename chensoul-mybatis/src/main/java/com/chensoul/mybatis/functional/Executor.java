package com.chensoul.mybatis.functional;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public interface Executor<T> {

    Optional<T> execute();

    Executor<T> onSuccess(Consumer<T> consumer);

    Executor<T> onError(Consumer<? super Throwable> consumer);
}
