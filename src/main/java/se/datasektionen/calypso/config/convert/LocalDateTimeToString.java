package se.datasektionen.calypso.config.convert;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;

public class LocalDateTimeToString implements Converter<LocalDateTime, String> {

	@Override
	public String convert(LocalDateTime source) {
		return source.toString();
	}

}
