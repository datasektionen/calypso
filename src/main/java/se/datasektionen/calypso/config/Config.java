package se.datasektionen.calypso.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class Config {
	private final String baseUrl;
	//TODO
	// private final String loginApiKey;
	// private final String loginFrontendUrl;
	// private final String loginApiUrl;

	private final String oidcProvider;
	private final String oidcId;
	private final String oidcSecret;
	private final String redirectUrl; //TODO unsure if this should even be here
	private final String darkmodeUrl;
	private final String hiveApiKey;
	private final String hiveApiUrl;

	public Config(
		@Value("${APPLICATION_URL}") String baseUrl,
		// @Value("${LOGIN_KEY}") String loginApiKey,
		// //also TODO
		// @Value("${LOGIN_FRONTEND_URL:https://login.datasektionen.se}") String loginFrontendUrl,
		// @Value("${LOGIN_API_URL:https://login.datasektionen.se}") String loginApiUrl,

		@Value("${OIDC_PROVIDER:https://sso.datasektionen.se}") String OIDCProvider,
		@Value("${OIDC_ID}") String OIDCId,
		@Value("${OIDC_SECRET}") String OIDCSecret,
		@Value("${REDIRECT_URL}") String redirectUrl, //i mean this is just baseUrl?

		@Value("${DARKMODE_URL:https://darkmode.datasektionen.se}") String darkmodeUrl,
		@Value("${HIVE_API_KEY}") String hiveApiKey,
		@Value("${HIVE_URL:https://hive.datasektionen.se}") String hiveApiUrl
	) {
		this.baseUrl = baseUrl;
		// this.loginApiKey = loginApiKey;
		// this.loginFrontendUrl = loginFrontendUrl;
		// this.loginApiUrl = loginApiUrl;
		this.oidcProvider = OIDCProvider;
		this.oidcId = OIDCId;
		this.oidcSecret = OIDCSecret;
		this.redirectUrl = redirectUrl;
		this.darkmodeUrl = darkmodeUrl;
		this.hiveApiKey = hiveApiKey;
		this.hiveApiUrl = hiveApiUrl;
	}
}