package se.datasektionen.calypso.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringBeans {

	@Bean
	public StringToLocalDateTimeConverter stringToLocalDateTimeConverter() {
		return new StringToLocalDateTimeConverter();
	}

}
