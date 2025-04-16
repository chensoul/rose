package com.chensoul.rose.mybatis.functional;

import java.util.function.Supplier;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public interface Create<T> {

    UpdateHandler<T> create(Supplier<T> supplier);
}
