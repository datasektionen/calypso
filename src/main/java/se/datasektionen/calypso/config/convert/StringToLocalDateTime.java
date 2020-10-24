package se.datasektionen.calypso.config.convert;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StringToLocalDateTime implements Converter<String, LocalDateTime> {

	@Override
	public LocalDateTime convert(String source) {
		// Trim and discard empty values
		source = source.trim();

		if (source.isEmpty())
			return null;

		// Convert to LocalDateTime
		var formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
		return LocalDateTime.parse(source, formatter);
	}

}
