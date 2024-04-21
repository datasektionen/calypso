package se.datasektionen.calypso;

import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import se.datasektionen.calypso.config.Config;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;

@Component
public class Darkmode {
	private final Config config;

	public Darkmode(Config config) {
		this.config = config;
	}

	public boolean getCurrent() {
		var url = config.getDarkmodeUrl();
		if (url == "true") return true;
		if (url == "false") return false;
		return new RestTemplate()
			.exchange(url, HttpMethod.GET, new HttpEntity<>(null, null), boolean.class)
			.getBody();
	}
}
