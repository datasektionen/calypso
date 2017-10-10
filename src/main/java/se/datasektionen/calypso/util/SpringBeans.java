package se.datasektionen.calypso.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.datasektionen.calypso.util.converters.LocalDateTimeConverter;
import se.datasektionen.calypso.util.converters.LocalDateTimeToStringConverter;
import se.datasektionen.calypso.util.converters.StringToLocalDateTimeConverter;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Configuration
public class SpringBeans {

	@Bean
	public StringToLocalDateTimeConverter stringToLocalDateTimeConverter() {
		return new StringToLocalDateTimeConverter();
	}

	@Bean
	public DateTimeFormatter formatter() {
		return DateTimeFormatter.ofPattern("d MMM YYYY HH:mm", Locale.forLanguageTag("sv"));
	}

	@Bean
	public LocalDateTimeToStringConverter localDateTimeToStringConverter() {
		return new LocalDateTimeToStringConverter();
	}

	@Bean
	public LocalDateTimeConverter localDateTimeConverter() {
		return new LocalDateTimeConverter();
	}

}
