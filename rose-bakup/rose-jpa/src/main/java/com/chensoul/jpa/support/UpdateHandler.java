package com.chensoul.jpa.support;

import java.util.function.Consumer;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public interface UpdateHandler<T> {

	Executor<T> update(Consumer<T> consumer);

}
