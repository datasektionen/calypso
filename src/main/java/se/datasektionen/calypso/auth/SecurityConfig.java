package se.datasektionen.calypso.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.addFilterBefore(customAuthFilter(), AbstractPreAuthenticatedProcessingFilter.class)
				.authorizeRequests()
					.mvcMatchers("/admin/**").authenticated()
					.and()
				.formLogin()
					.loginPage("/auth/login")
					.permitAll()
					.and()
				.authenticationProvider(new DAuthenticationProvider());
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(preauthAuthProvider());
	}

	@Bean
	public PreAuthenticatedAuthenticationProvider preauthAuthProvider() {
		PreAuthenticatedAuthenticationProvider preauthAuthProvider =
				new PreAuthenticatedAuthenticationProvider();
		preauthAuthProvider.setPreAuthenticatedUserDetailsService(
				userDetailsServiceWrapper());
		return preauthAuthProvider;
	}

	@Bean
	public OnlyRolesPreAuthenticatedUserDetailsService userDetailsServiceWrapper() {
		OnlyRolesPreAuthenticatedUserDetailsService service =
				new MyPreAuthenticatedUserDetailsService();
		return service;
	}

	@Bean
	public DAuthProcessingFilter customAuthFilter() throws Exception {
		DAuthProcessingFilter filter = new DAuthProcessingFilter();
		filter.setAuthenticationManager(authenticationManager());
		return filter;
	}

}