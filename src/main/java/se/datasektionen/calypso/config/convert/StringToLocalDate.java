package se.datasektionen.calypso.config.convert;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;

public class StringToLocalDate implements Converter<String, LocalDate> {

	@Override
	public LocalDate convert(String source) {
		return LocalDate.parse(source);
	}

}
