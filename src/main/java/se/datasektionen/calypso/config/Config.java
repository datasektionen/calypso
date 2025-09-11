package se.datasektionen.calypso.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Config {
	private final String baseUrl;
	private final String loginApiKey;
	private final String loginFrontendUrl;
	private final String loginApiUrl;
	private final String darkmodeUrl;
	private final String hiveApiKey;

	public Config(
		@Value("${APPLICATION_URL}") String baseUrl,
		@Value("${LOGIN_KEY}") String loginApiKey,
		@Value("${LOGIN_FRONTEND_URL:https://login.datasektionen.se}") String loginFrontendUrl,
		@Value("${LOGIN_API_URL:https://login.datasektionen.se}") String loginApiUrl,
		@Value("${DARKMODE_URL:https://darkmode.datasektionen.se}") String darkmodeUrl,
		@Value("${HIVE_API_KEY}") String hiveApiKey
	) {
		this.baseUrl = baseUrl;
		this.loginApiKey = loginApiKey;
		this.loginFrontendUrl = loginFrontendUrl;
		this.loginApiUrl = loginApiUrl;
		this.darkmodeUrl = darkmodeUrl;
		this.hiveApiKey = hiveApiKey;
	}
}
