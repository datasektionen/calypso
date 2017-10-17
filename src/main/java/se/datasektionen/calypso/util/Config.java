package se.datasektionen.calypso.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Config {

	@Value("${APPLICATION_URL}")
	private String baseUrl;

	@Value("${LOGIN2_KEY}")
	private String apiKey;

	public String getBaseUrl() {
		return baseUrl;
	}

	public String getApiKey() {
		return apiKey;
	}
}
