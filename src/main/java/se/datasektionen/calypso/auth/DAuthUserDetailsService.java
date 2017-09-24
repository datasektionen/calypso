package se.datasektionen.calypso.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

		String user = response.getUser();
		String plsUrl = "http://pls.datasektionen.se/api/user/" + user + "/prometheus";

		HttpHeaders headers = new HttpHeaders();
		headers.set("User-Agent", "Spring Framework/Java " + System.getProperty("java.version"));

		ResponseEntity<String[]> permissions = new RestTemplate()
				.exchange(plsUrl, HttpMethod.GET, new HttpEntity<>(null, headers), String[].class);
		List<GrantedAuthority> authorities = Arrays.stream(permissions.getBody())
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		// Successful login
		return new DAuthUserDetails(
				response.getUser(),
				token.toString(),
				response.getFirst_name(),
				response.getLast_name(),
				response.getEmails(),
				authorities
		);
	}

}
