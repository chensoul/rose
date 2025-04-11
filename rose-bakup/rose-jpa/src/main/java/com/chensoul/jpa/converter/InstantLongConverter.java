package com.chensoul.jpa.converter;

import javax.persistence.AttributeConverter;
import java.time.Instant;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class InstantLongConverter implements AttributeConverter<Instant, Long> {

	@Override
	public Long convertToDatabaseColumn(Instant date) {
		return date == null ? null : date.toEpochMilli();
	}

	@Override
	public Instant convertToEntityAttribute(Long date) {
		return date == null ? null : Instant.ofEpochMilli(date);
	}
}
