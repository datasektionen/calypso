package se.datasektionen.calypso.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import se.datasektionen.calypso.config.convert.LocalDateTimeToString;
import se.datasektionen.calypso.config.convert.LocalDateToString;
import se.datasektionen.calypso.config.convert.LocalTimeToString;
import se.datasektionen.calypso.config.convert.PeriodToString;
import se.datasektionen.calypso.config.convert.StringToLocalDate;
import se.datasektionen.calypso.config.convert.StringToLocalDateTime;
import se.datasektionen.calypso.config.convert.StringToLocalTime;
import se.datasektionen.calypso.config.convert.StringToPeriod;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
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

	@Bean
	public Converter<String, LocalDate> stringToLocalDateConverter() {
		return new StringToLocalDate();
	}

	@Bean
	public Converter<LocalDate, String> localDateToStringConverter() {
		return new LocalDateToString();
	}

	@Bean
	public Converter<String, LocalTime> stringToLocalTimeConverter() {
		return new StringToLocalTime();
	}

	@Bean
	public Converter<LocalTime, String> localTimeToStringConverter() {
		return new LocalTimeToString();
	}

	@Bean
	public Converter<String, Period> stringToPeriodConverter() {
		return new StringToPeriod();
	}

	@Bean
	public Converter<Period, String> periodToStringConverter() {
		return new PeriodToString();
	}

}
