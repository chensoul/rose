package com.chensoul.core.util.lang.pipeline;

/**
 * @param <I>
 * @param <O>
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 */
@FunctionalInterface
public interface Handler<I, O> {
	O process(I input);
}
