package se.datasektionen.calypso.auth;

import se.datasektionen.calypso.config.Config;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // only if you really want this
            .authorizeHttpRequests(auth -> auth
                .mvcMatchers("/debug/**").permitAll()
                .mvcMatchers("/admin/**").authenticated()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth -> oauth
                .userInfoEndpoint(userInfo -> userInfo
                    .oidcUserService(oidcUserService())
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

                OidcUser oidcUser = delegate.loadUser(userRequest);

                Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

                var permissionObj = oidcUser.getAttributes().get("permissions");

                if (!(permissionObj instanceof JSONArray)) {
                    throw new OAuth2AuthenticationException(
                        new OAuth2Error("Data fetched from OIDC is not a JSONArray: " + permissionObj),
                        "User not authorized" //TODO: test
                    );
                }

                JSONArray permissionsArray = (JSONArray) permissionObj;

                for (Object item : permissionsArray) {
                    if (!(item instanceof JSONObject)) {
                        // Very unsure if this will ever run
                        throw new OAuth2AuthenticationException(
                            new OAuth2Error("Data inside of JSONArray is not a JSONObject: " + item),
                            "User not authorized"
                        );
                    }

                    JSONObject permission = (JSONObject) item;

                    String id = permission.getAsString("id");

                    if (id != null) {
                        mappedAuthorities.add(new SimpleGrantedAuthority(id));
                    }
                    //TODO, maybe print a warning that the user has null authorties (somehjow)
                }
                
                System.out.println("Permissions type: " + permissionObj.getClass());
                System.out.println(mappedAuthorities);

                return new DefaultOidcUser(
                        mappedAuthorities,
                        oidcUser.getIdToken(),
                        oidcUser.getUserInfo()
                );
            }
        };
    }
}
