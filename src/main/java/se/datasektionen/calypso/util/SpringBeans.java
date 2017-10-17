package se.datasektionen.calypso.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
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

	@Bean
	@Primary
	public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
		ObjectMapper objectMapper = builder.createXmlMapper(false).build();
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		return objectMapper;
	}

}
