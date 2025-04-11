package com.chensoul.jpa.support;

import java.util.function.Supplier;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public interface Loader<T, ID> {

	UpdateHandler<T> loadById(ID id);

	UpdateHandler<T> load(Supplier<T> t);

}
