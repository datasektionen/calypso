package se.datasektionen.calypso.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import se.datasektionen.calypso.util.Config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DAuthEntryPoint implements AuthenticationEntryPoint {
	private Config config;

	@Autowired
	public DAuthEntryPoint(Config config) {
		this.config = config;
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
			throws IOException {
		response.sendRedirect("https://login2.datasektionen.se/login?callback=" +
				config.getBaseUrl() + "/auth/verify?token=");
	}
}
