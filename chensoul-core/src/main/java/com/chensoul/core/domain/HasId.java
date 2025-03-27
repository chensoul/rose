package com.chensoul.core.domain;

import java.io.Serializable;

/**
 * @param <I>
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 */
public interface HasId<I extends Serializable> extends Serializable {
	I getId();
}
