package io.github.jotabrc.security;

import io.github.jotabrc.ov_auth_validator.util.UserRoles;
import io.github.jotabrc.ovauth.TokenGlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
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
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(SWAGGER_WHITELIST).permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers(
                                MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/inventory/add",
                                MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/inventory/get/uuid/",
                                MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/item/add",
                                MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/item/get/uuid/",
                                MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/item/update/stock",
                                MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/item/update/name",
                                MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/item/update/reserve",
                                MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/location/add",
                                MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/location/get/uuid/",
                                MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/location/update"
                        ).hasRole(UserRoles.SYSTEM.getName())
                        .anyRequest().authenticated()
                )
                .addFilterAfter(new TokenGlobalFilter(), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        return http.build();
    }
}
