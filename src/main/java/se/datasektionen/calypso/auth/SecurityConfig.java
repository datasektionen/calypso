package se.datasektionen.calypso.auth;

import se.datasektionen.calypso.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // only if you really want this
            .authorizeHttpRequests(auth -> auth
                .mvcMatchers("/public/**", "/login").permitAll()
                .mvcMatchers("/admin/**").authenticated()
                .anyRequest().authenticated()
            )
            .oauth2Login();

        return http.getOrBuild();
    }
}
