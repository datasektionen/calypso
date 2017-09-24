package se.datasektionen.calypso.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf()
				.disable()
				.addFilterBefore(dAuthFilter(), BasicAuthenticationFilter.class)
				.authorizeRequests()
					.mvcMatchers("/admin/**").authenticated()
					.and()
				.authenticationProvider(dAuthAuthProvider())
				.exceptionHandling()
				.authenticationEntryPoint(dAuthEntryPoint());
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(dAuthAuthProvider());
	}

	@Bean
	public PreAuthenticatedAuthenticationProvider dAuthAuthProvider() {
		DAuthProvider provider = new DAuthProvider();
		provider.setPreAuthenticatedUserDetailsService(dAuthUserDetails());

		return provider;
	}

	@Bean
	public DAuthUserDetailsService dAuthUserDetails() {
		return new DAuthUserDetailsService();
	}

	@Bean
	public DAuthEntryPoint dAuthEntryPoint() {
		return new DAuthEntryPoint();
	}

	@Bean
	public DAuthFilter dAuthFilter() throws Exception {
		DAuthFilter filter = new DAuthFilter();
		filter.setAuthenticationManager(authenticationManager());
		return filter;
	}

}