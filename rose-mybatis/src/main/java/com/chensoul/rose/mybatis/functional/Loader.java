package com.chensoul.rose.mybatis.functional;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public interface Loader<T> {

    UpdateHandler<T> loadById(Serializable id);

    UpdateHandler<T> load(Supplier<T> t);
}
