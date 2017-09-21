package se.datasektionen.calypso.auth;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
					.mvcMatchers("/admin/**").authenticated()
					.and()
				.formLogin()
					.loginPage("/auth/login")
					.permitAll()
					.and()
				.authenticationProvider(new DAuthenticationProvider());
	}
}