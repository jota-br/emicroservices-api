package io.github.jotabrc.security;

import io.github.jotabrc.ovauth.TokenGlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static io.github.jotabrc.controller.ControllerDefaults.MAPPING_PREFIX;
import static io.github.jotabrc.controller.ControllerDefaults.MAPPING_VERSION_SUFFIX;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static final String[] SWAGGER_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/webjars/**"
    };

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/order/add").hasRole("SYSTEM")
                        .requestMatchers(MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/order/get/uuid/").hasRole("SYSTEM")
                        .requestMatchers(MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/order/get/user/uuid/").hasRole("SYSTEM")
                        .requestMatchers(MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/order/update").hasRole("SYSTEM")
                        .requestMatchers(MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/order/cancel/uuid/").hasRole("SYSTEM")
                        .requestMatchers(MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/order/return").hasRole("SYSTEM")
                        .requestMatchers(SWAGGER_WHITELIST).permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterAfter(new TokenGlobalFilter(), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        return http.build();
    }
}
