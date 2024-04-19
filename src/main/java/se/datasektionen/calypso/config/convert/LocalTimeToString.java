package se.datasektionen.calypso.config.convert;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalTime;

public class LocalTimeToString implements Converter<LocalTime, String> {

	@Override
	public String convert(LocalTime source) {
		return source.toString();
	}

}
