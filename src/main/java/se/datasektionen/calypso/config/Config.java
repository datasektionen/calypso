package se.datasektionen.calypso.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Config {

	private final String baseUrl;
	private final String apiKey;
	private final String receptionMode;

	public Config(@Value("${APPLICATION_URL}") String baseUrl, @Value("${LOGIN_KEY}") String apiKey, @Value("${RECEPTION_MODE}") String receptionMode) {
		this.baseUrl = baseUrl;
		this.apiKey = apiKey;
		this.receptionMode = receptionMode;
	}

}
