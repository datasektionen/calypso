package se.datasektionen.calypso.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Config {
	private final String baseUrl;
	private final String loginApiKey;
	private final String loginUrl;

	public Config(
		@Value("${APPLICATION_URL}") String baseUrl,
		@Value("${LOGIN_KEY}") String loginApiKey,
		@Value("${LOGIN_URL:https://login.datasektionen.se}") String loginUrl
	) {
		this.baseUrl = baseUrl;
		this.loginApiKey = loginApiKey;
		this.loginUrl = loginUrl;
	}
}
