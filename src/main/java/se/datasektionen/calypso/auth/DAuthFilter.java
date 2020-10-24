package se.datasektionen.calypso.auth;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DAuthFilter extends AbstractAuthenticationProcessingFilter {

	public DAuthFilter() {
		super(new AntPathRequestMatcher("/auth/verify"));
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		var token = request.getParameter("token");

		System.out.println("Filter running, token is " + token);

		if (token == null || token.isEmpty())
			throw new BadCredentialsException("Invalid authentication token");

		return new PreAuthenticatedAuthenticationToken(token, token);
	}
}
