package se.datasektionen.calypso.config.convert;

import org.springframework.core.convert.converter.Converter;

import java.time.Period;

public class PeriodToString implements Converter<Period, String> {

	@Override
	public String convert(Period source) {
		return source.normalized().toString();
	}

}
