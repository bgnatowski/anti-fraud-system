package pl.bgnat.antifraudsystem.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static pl.bgnat.antifraudsystem.user.Role.*;

@Configuration
@EnableWebSecurity
public class SecurityFilterChainConfig {
	private final AuthenticationProvider authenticationProvider;
	private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

	public SecurityFilterChainConfig(AuthenticationProvider authenticationProvider,
									 RestAuthenticationEntryPoint authenticationEntryPoint) {
		this.authenticationProvider = authenticationProvider;
		this.restAuthenticationEntryPoint = authenticationEntryPoint;
	}


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.httpBasic()
				.and()
				.csrf().disable().headers().frameOptions().disable()
				.and()
				.authorizeHttpRequests()
				.requestMatchers(HttpMethod.GET,
						"/api/auth/list").hasAnyAuthority(ADMINISTRATOR.name(), SUPPORT.name())
				.requestMatchers(HttpMethod.DELETE,
						"/api/auth/user/*").hasAuthority(ADMINISTRATOR.name())
				.requestMatchers(HttpMethod.POST,
						"/api/antifraud/transaction").hasAuthority(MERCHANT.name())
				.requestMatchers(HttpMethod.PUT,
						"/api/auth/access",
						"/api/auth/access/",
						"/api/auth/role",
						"/api/auth/role/").hasAuthority(ADMINISTRATOR.name())
				.requestMatchers("/actuator/shutdown").permitAll()
				.requestMatchers(HttpMethod.POST,
						"/api/auth/user").permitAll() //Anonymous
				.anyRequest()
				.authenticated()
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authenticationProvider(authenticationProvider)
				.exceptionHandling()
				.authenticationEntryPoint(restAuthenticationEntryPoint);
		return http.build();
	}


}
