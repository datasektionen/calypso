package se.datasektionen.calypso.auth;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.reactive.function.client.WebClient;
import se.datasektionen.calypso.auth.entities.Group;
import se.datasektionen.calypso.config.Config;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private Config config;
    private final WebClient webClient;

    @Autowired
    public SecurityConfig(Config config, WebClient webClient) {
        this.config = config;
        this.webClient = webClient;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http)
        throws Exception {
        http
            .csrf()
            .disable()
            .authorizeHttpRequests()
            .mvcMatchers("/admin/**")
            .authenticated()
            .and()
            .oauth2Login(oauth ->
                oauth.userInfoEndpoint(userInfo ->
                    userInfo.oidcUserService(oidcUserService())
                )
            );

        return http.getOrBuild();
    }

    @Bean
    public OidcUserService oidcUserService() {
        OidcUserService delegate = new OidcUserService();

        return new OidcUserService() {
            @Override
            public OidcUser loadUser(OidcUserRequest userRequest)
                throws OAuth2AuthenticationException {
                OidcUser user = delegate.loadUser(userRequest);
                Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
                var permissionObj = user.getAttributes().get("permissions");

                if (!(permissionObj instanceof JSONArray)) {
                    throw new OAuth2AuthenticationException(
                        new OAuth2Error(
                            "Data fetched from OIDC is not a JSONArray: " +
                                permissionObj
                        ),
                        "User not authorized"
                    );
                }

                JSONArray permissionsArray = (JSONArray) permissionObj;

                for (Object item : permissionsArray) {
                    JSONObject permission = (JSONObject) item;
                    String id = permission.getAsString("id");
                    if (id != null) {
                        mappedAuthorities.add(new SimpleGrantedAuthority(id));
                    }
                }

                var mandatesUrl =
                    config.getHiveApiUrl() +
                    "/api/v1/tagged/author-pseudonym/memberships/" +
                    user.getName();

                List<Group> groups = webClient
                    .get()
                    .uri(mandatesUrl)
                    .headers(h -> h.setBearerAuth(config.getHiveApiKey()))
                    .retrieve()
                    .bodyToMono(
                        new ParameterizedTypeReference<List<Group>>() {}
                    )
                    .block();

                Map<String, String> mandates = groups
                    .stream()
                    .collect(
                        Collectors.toMap(
                            Group::getGroup_id,
                            Group::getGroup_name
                        )
                    );

                Map<String, Object> attributes = new HashMap<>(
                    user.getClaims()
                );

                attributes.put("mandates", mandates);

                return new DefaultOidcUser(
                    mappedAuthorities,
                    user.getIdToken(),
                    new OidcUserInfo(attributes)
                );
            }
        };
    }
}
