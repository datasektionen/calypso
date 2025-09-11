package se.datasektionen.calypso.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
import se.datasektionen.calypso.config.Config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>Resolves {@link PreAuthenticatedAuthenticationToken}'s into {@link DAuthUserDetails} objects.</p>
 *
 * <p>Does so by using the <pre>login</pre> service.</p>
 */
public class DAuthUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

	private Config config;

	@Autowired
	public DAuthUserDetailsService(Config config) {
		this.config = config;
	}

	@Override
	public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
		// Required variables
		var t = token.getPrincipal().toString();
		var url = config.getLoginApiUrl() + "/verify/" + t + ".json?api_key=" + config.getLoginApiKey();

		var headers = new HttpHeaders();
		headers.set("User-Agent", "Spring Framework/Java " + System.getProperty("java.version"));

		// Perform REST consumption
		var response = new RestTemplate()
			.exchange(url, HttpMethod.GET, new HttpEntity<>(null, headers), DAuthResponse.class)
			.getBody();

		System.err.println("Received user question for user " + token);
		System.out.println("Fetched user: " + response.toString());

		// Unsuccessful login
		if (response.getUser() == null || response.getFirstName() == null)
			throw new UsernameNotFoundException("Token rendered empty or malformed response");

		// Prepare hive API calls
		var user = response.getUser();
		var permissionsUrl = "https://hive.datasektionen.se/api/v1/user/" + user + "/permissions";

		headers.set("Authorization", "Bearer " + config.getHiveApiKey());

		// Read permissions from hive
		var permissions = new RestTemplate()
				.exchange(permissionsUrl, HttpMethod.GET, new HttpEntity<>(null, headers), String[].class);
		List<GrantedAuthority> authorities = Arrays.stream(permissions.getBody())
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		// Try to get mandates from hive
		Map<String, String> mandates = new HashMap<>();
		var mandatesUrl = "https://hive.datasektionen.se/api/v1/tagged/author-pseudonym/memberships/"+user;
		try {
			var mandatesHttp = new RestTemplate()
					.exchange(mandatesUrl, HttpMethod.GET, new HttpEntity<>(null, headers), DFunktResponse.class);

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
				response.getFirstName(),
				response.getLastName(),
				response.getEmails(),
				mandates,
				authorities
		);
	}

}
