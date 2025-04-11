package com.chensoul.jpa.support;

import java.util.function.Supplier;
import javax.validation.groups.Default;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public interface Create<T> extends Default {

	UpdateHandler<T> create(Supplier<T> supplier);

}
