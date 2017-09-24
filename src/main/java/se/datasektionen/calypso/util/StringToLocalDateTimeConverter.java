package se.datasektionen.calypso.util;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {
	public LocalDateTime convert(String source) {
		// Discard null values
		if (source == null)
			return null;

		// Trim and discard empty values
		source = source.trim();

		if (source.isEmpty())
			return null;

		// Convert to LocalDateTime
		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
		return LocalDateTime.parse(source, formatter);
	}
}