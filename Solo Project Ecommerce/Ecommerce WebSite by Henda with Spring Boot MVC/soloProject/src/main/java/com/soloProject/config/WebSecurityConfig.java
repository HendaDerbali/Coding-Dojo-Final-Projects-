package com.soloProject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
public class WebSecurityConfig {

	private UserDetailsService userDetailsService;
	@Autowired
	HandlerMappingIntrospector introspector;

	// Bean for encoding passwords
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// Security filter chain configuration
	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers(new MvcRequestMatcher(introspector, "/css/**"),
						new MvcRequestMatcher(introspector, "/js/**"), new MvcRequestMatcher(introspector, "/register"),
						new MvcRequestMatcher(introspector, "/login"))
				.permitAll()
				.requestMatchers(new MvcRequestMatcher(introspector, "/delete/"),
						new MvcRequestMatcher(introspector, "/admin/**"))
				.hasAnyRole("SUPER_ADMIN", "ADMIN").requestMatchers(new MvcRequestMatcher(introspector, "/home"))
				.authenticated().anyRequest().permitAll())
				.formLogin(form -> form.loginPage("/login").usernameParameter("email") // Use email instead of userName
																						// for login purposes
						.permitAll().successHandler(loginSuccessHandler())) // Use the custom success handler
				.logout(logout -> logout.permitAll());

		return http.build();
	}

	// Bean for handling successful login and redirecting to /home
	@Bean
	public AuthenticationSuccessHandler loginSuccessHandler() {
		return (request, response, authentication) -> {
			response.sendRedirect("/home"); // Redirect to /home upon successful login
		};
	}

	// Configure global authentication manager
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}

}
