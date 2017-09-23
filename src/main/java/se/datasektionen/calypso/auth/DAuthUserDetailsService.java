package se.datasektionen.calypso.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.client.RestTemplate;

/**
 * <p>Resolves {@link PreAuthenticatedAuthenticationToken}'s into {@link DAuthUserDetails} objects.</p>
 *
 * <p>Does so by using the <pre>login2</pre> service.</p>
 */
public class DAuthUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

	@Value("${LOGIN2_KEY}")
	private String apiKey;

	@Override
	public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
		// Required variables
		String t = token.getPrincipal().toString();
		String url = "https://login2.datasektionen.se/verify/" + t + ".json?api_key=" + apiKey;

		// Perform REST consumption
		DAuthResponse response = new RestTemplate().getForObject(url, DAuthResponse.class);

		System.err.println("Received user question for user " + token);
		System.out.println("Fetched user: " + response.toString());

		// Unsuccessful login
		if (response.getUser() == null || response.getFirst_name() == null)
			throw new UsernameNotFoundException("Token rendered empty or malformed response");

		// Successful login
		return new DAuthUserDetails(
				response.getUser(),
				token.toString(),
				response.getFirst_name(),
				response.getLast_name(),
				response.getEmails()
		);
	}

}
