package com.solution.apigateway.security;

import com.solution.apigateway.filter.AccountValidationFilter;
import com.solution.apigateway.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String[] PUBLIC_PATHS = {
        "/api/auth/**"
    };

//    private static final String[] ANONYMOUS_PATHS = {
//        ""
//    };

    private static final String[] AUTHENTICATED_PATHS = {
        "/api/**"
    };

    private final JwtAuthFilter authFilter;
    private final AccountValidationFilter  accountValidationFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_PATHS).permitAll()
//                        .requestMatchers(ANONYMOUS_PATHS).anonymous()
                        .requestMatchers(AUTHENTICATED_PATHS).authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(accountValidationFilter, JwtAuthFilter.class);
        return http.build();
    }
}
