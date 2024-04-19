package se.datasektionen.calypso.config.convert;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;

public class LocalDateToString implements Converter<LocalDate, String> {

	@Override
	public String convert(LocalDate source) {
		return source.toString();
	}

}
