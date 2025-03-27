package com.chensoul.core.util.date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BetweenFormatterTest {

	@Test
	void format() {
		BetweenFormatter betweenFormatter = new BetweenFormatter(60000, BetweenFormatter.Level.MINUTE);
		Assertions.assertEquals("1分钟", betweenFormatter.format());
		Assertions.assertEquals("1分钟", betweenFormatter.toString());
	}
}
