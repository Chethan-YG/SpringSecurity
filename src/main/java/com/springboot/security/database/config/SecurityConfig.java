package com.springboot.security.database.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import com.springboot.security.database.Service.CustomUserService;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	@Autowired
	private OauthAuthenticationSuccessHandler handler;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	private CustomUserService customUserService;


	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider Provider = new DaoAuthenticationProvider();

		Provider.setUserDetailsService(this.customUserService);
		Provider.setPasswordEncoder(passwordEncoder());

		return Provider;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {

		security.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(
						registry -> registry.requestMatchers("/register", "/login", "/welcome",
											"/forgot-password","/reset-password","/submit-otp","/update-password")
											.permitAll()
											.requestMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN")
											.requestMatchers("/user/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
											.anyRequest().authenticated())
											.formLogin(login -> login.loginPage("/login")
											.loginProcessingUrl("/login")
											.defaultSuccessUrl("/welcome", true)
											.failureUrl("/login?error").permitAll())
											.logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
											.permitAll())
											.oauth2Login(oauth2 -> {
												oauth2.loginPage("/login");
												oauth2.successHandler(handler);
												oauth2.failureUrl("/Error");
											});
											
										   
												
		security.authenticationProvider(daoAuthenticationProvider());
		DefaultSecurityFilterChain build = security.build();

		return build;
		

	}	
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
		return new MethodValidationPostProcessor();
	}

}