package se.datasektionen.calypso.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import se.datasektionen.calypso.config.convert.LocalDateTimeToString;
import se.datasektionen.calypso.config.convert.StringToLocalDateTime;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Configuration
public class DateConfig {

	@Bean
	public DateTimeFormatter formatter() {
		return DateTimeFormatter.ofPattern("d MMM YYYY HH:mm", Locale.forLanguageTag("sv"));
	}

	@Bean
	public Converter<String, LocalDateTime> stringToLocalDateTimeConverter() {
		return new StringToLocalDateTime();
	}

	@Bean
	public Converter<LocalDateTime, String> localDateTimeToStringConverter() {
		return new LocalDateTimeToString();
	}

}
