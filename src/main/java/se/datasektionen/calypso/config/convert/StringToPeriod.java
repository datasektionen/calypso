package se.datasektionen.calypso.config.convert;

import org.springframework.core.convert.converter.Converter;

import java.time.Period;

public class StringToPeriod implements Converter<String, Period> {

	@Override
	public Period convert(String source) {
		return Period.parse(source).normalized();
	}

}
