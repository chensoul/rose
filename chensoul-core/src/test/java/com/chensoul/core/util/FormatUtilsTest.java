package com.chensoul.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link FormatUtils} Test
 *
 * @since 1.0.0
 */
public class FormatUtilsTest {

	@Test
	public void testFormat() {
		String message = FormatUtils.format("A,{},C,{},E", "B", "D");
		assertEquals("A,B,C,D,E", message);

		message = FormatUtils.format("A,{},C,{},E", "B");
		assertEquals("A,B,C,{},E", message);

		message = FormatUtils.format("A,{},C,{},E");
		assertEquals("A,{},C,{},E", message);

		message = FormatUtils.format("A,{},C,{},E", 1, 2, 3);
		assertEquals("A,1,C,2,E", message);
	}

}
