package se.datasektionen.calypso.util.converters;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;

public class LocalDateTimeToStringConverter implements Converter<LocalDateTime, String> {

	@Override
	public String convert(LocalDateTime source) {
		// Discard null values
		if (source == null)
			return "";

		return source.toString();
	}

}