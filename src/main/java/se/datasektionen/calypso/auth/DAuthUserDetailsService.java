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
import se.datasektionen.calypso.auth.entities.DAuthResponse;
import se.datasektionen.calypso.auth.entities.dfunkt.DFunktResponse;
import se.datasektionen.calypso.auth.entities.dfunkt.Mandate;
import se.datasektionen.calypso.auth.entities.dfunkt.Role;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

		// Prepare Pls and Dfunkt API calls
		String user = response.getUser();
		String plsUrl = "http://pls.datasektionen.se/api/user/" + user + "/prometheus";

		HttpHeaders headers = new HttpHeaders();
		headers.set("User-Agent", "Spring Framework/Java " + System.getProperty("java.version"));

		// Read permissions from Pls
		ResponseEntity<String[]> permissions = new RestTemplate()
				.exchange(plsUrl, HttpMethod.GET, new HttpEntity<>(null, headers), String[].class);
		List<GrantedAuthority> authorities = Arrays.stream(permissions.getBody())
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		// Try to get mandates from DFunkt
		Map<String, String> mandates = new HashMap<>();
		String dFunktUrl = "http://dfunkt.datasektionen.se/api/user/kthid/" + user; // + "/current";
		try {
			ResponseEntity<DFunktResponse> mandatesHttp = new RestTemplate()
					.exchange(dFunktUrl, HttpMethod.GET, new HttpEntity<>(null, headers), DFunktResponse.class);

			mandates = mandatesHttp
					.getBody()
					.getMandates()
					.stream()
					.map(Mandate::getRole)
					.collect(Collectors.toMap(Role::getIdentifier, Role::getTitle));

			System.out.println(mandates.toString());
		} catch (Exception ignore) {
			ignore.printStackTrace();
		}

		// Successful login
		return new DAuthUserDetails(
				response.getUser(),
				token.toString(),
				response.getFirst_name(),
				response.getLast_name(),
				response.getEmails(),
				mandates,
				authorities
		);
	}

}
