package pl.bgnat.antifraudsystem.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityFilterChainConfig {
    private final AuthenticationProvider authenticationProvider;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(conf -> conf.authenticationEntryPoint(restAuthenticationEntryPoint))
                .csrf(csrfConf -> csrfConf.disable())
                .headers(headersConf -> headersConf.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()))
                .authorizeHttpRequests(
                        auth -> auth
                                    .requestMatchers(HttpMethod.DELETE, "/api/auth/user/*").hasAuthority("ADMINISTRATOR")
                                    .requestMatchers(HttpMethod.GET, "/api/auth/users").hasAnyAuthority("ADMINISTRATOR", "SUPPORT")
                                    .requestMatchers(HttpMethod.POST, "/api/antifraud/transaction", "/api/antifraud/transaction/").hasAnyAuthority("MERCHANT", "SUPPORT", "ADMINISTRATOR")
                                    .requestMatchers(HttpMethod.PUT, "/api/auth/access", "/api/auth/access/", "/api/auth/role", "/api/auth/role/").hasAuthority("ADMINISTRATOR")
                                    .requestMatchers(HttpMethod.POST, "api/antifraud/suspicious-ip", "api/antifraud/suspicious-ip/", "api/antifraud/stolencard", "api/antifraud/stolencard/", "/api/antifraud/suspicious-ip", "/api/antifraud/suspicious-ip/", "/api/antifraud/stolencard", "/api/antifraud/stolencard/").hasAnyAuthority("SUPPORT", "ADMINISTRATOR")
                                    .requestMatchers(HttpMethod.DELETE, "api/antifraud/suspicious-ip", "api/antifraud/suspicious-ip/*", "api/antifraud/stolencard", "api/antifraud/stolencard/*", "/api/antifraud/suspicious-ip", "/api/antifraud/suspicious-ip/*", "/api/antifraud/stolencard", "/api/antifraud/stolencard/*").hasAnyAuthority("SUPPORT", "ADMINISTRATOR")
                                    .requestMatchers(HttpMethod.GET, "api/antifraud/suspicious-ip", "api/antifraud/suspicious-ip/", "api/antifraud/stolencard", "api/antifraud/stolencard/", "/api/antifraud/suspicious-ip", "/api/antifraud/suspicious-ip/", "/api/antifraud/stolencard", "/api/antifraud/stolencard/").hasAnyAuthority("SUPPORT", "ADMINISTRATOR")
                                    .requestMatchers("/actuator/shutdown").permitAll()
                                    .requestMatchers(HttpMethod.POST, "/api/auth/user").permitAll()
                                    .requestMatchers(HttpMethod.PATCH, "/api/auth/user/**").permitAll()
                                    .anyRequest().authenticated()
                )
                .sessionManagement(conf -> conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .exceptionHandling(conf -> conf.authenticationEntryPoint(restAuthenticationEntryPoint));
        return http.build();
    }
}
