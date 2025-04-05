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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

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

                        .requestMatchers(MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/user/login").permitAll()
                        .requestMatchers(MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/user/register").permitAll()

                        .requestMatchers(MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/user/add/address")
                        .hasAnyRole(UserRoles.USER.getName(), UserRoles.ADMIN.getName())

                        .requestMatchers(MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/user/update")
                        .hasAnyRole(UserRoles.USER.getName(), UserRoles.ADMIN.getName())

                        .requestMatchers(MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/user/update/password")
                        .hasAnyRole(UserRoles.USER.getName(), UserRoles.ADMIN.getName())

                        .requestMatchers(MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/user/get/uuid/")
                        .hasAnyRole(UserRoles.USER.getName(), UserRoles.ADMIN.getName())

                        .requestMatchers(MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/address/get/uuid/")
                        .hasAnyRole(UserRoles.USER.getName(), UserRoles.ADMIN.getName())

                        .anyRequest().permitAll()
                )
                .addFilterAfter(new TokenGlobalFilter(), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8080", "http://localhost:8080/**"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:8080")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("Authorization", "Content-Type")
                        .exposedHeaders("Authorization")
                        .allowCredentials(true);
            }
        };
    }

}
