package se.datasektionen.calypso.config.convert;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalTime;

public class StringToLocalTime implements Converter<String, LocalTime> {

	@Override
	public LocalTime convert(String source) {
		return LocalTime.parse(source);
	}

}
