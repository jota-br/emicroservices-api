package io.github.jotabrc.config;

import io.github.jotabrc.ov_auth_validator.util.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
public class GatewayConfig implements WebFluxConfigurer {

    @Autowired
    private ServiceConfiguration serviceConfig;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        System.out.println(serviceConfig.getUserServiceReplacement());
        return builder.routes()
                .route(serviceConfig.getUserServiceName(), r -> r.path("/user/**")
                        .filters(f -> f
                                .rewritePath(serviceConfig.getUserServicePattern(), serviceConfig.getUserServiceReplacement())
                        )
                        .uri(serviceConfig.getUserServiceUri()))

                .route(serviceConfig.getUserActivationServiceName(), r -> r.path("/activation-token/**")
                        .filters(f -> f
                                .rewritePath(serviceConfig.getUserActivationServicePattern(), serviceConfig.getUserActivationServiceReplacement())
                        )
                        .uri(serviceConfig.getUserActivationServiceUri()))

                .route(serviceConfig.getInventoryServiceName(), r -> r.path("/inventory/**")
                        .filters(f -> f
                                .rewritePath(serviceConfig.getInventoryServicePattern(), serviceConfig.getInventoryServiceReplacement())
                        )
                        .uri(serviceConfig.getInventoryServiceUri()))

                .route(serviceConfig.getOrderServiceName(), r -> r.path("/order/**")
                        .filters(f -> f
                                .rewritePath(serviceConfig.getOrderServicePattern(), serviceConfig.getOrderServiceReplacement())
                        )
                        .uri(serviceConfig.getOrderServiceUri()))

                .route(serviceConfig.getProductServiceName(), r -> r.path("/product/**")
                        .filters(f -> f
                                .rewritePath(serviceConfig.getProductServicePattern(), serviceConfig.getProductServiceReplacement())
                        )
                        .uri(serviceConfig.getProductServiceUri()))
                .build();
    }

    @Bean
    protected SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(auth -> auth
                        // User API
                        .pathMatchers("/user/login").permitAll()
                        .pathMatchers("/user/register").permitAll()
                        .pathMatchers("/activation-token/activate/**").permitAll()

                        .pathMatchers("/user/add/address")
                        .hasAnyRole(UserRoles.USER.getName(), UserRoles.ADMIN.getName())

                        .pathMatchers("/user/update")
                        .hasAnyRole(UserRoles.USER.getName(), UserRoles.ADMIN.getName())

                        .pathMatchers("/user/update/password")
                        .hasAnyRole(UserRoles.USER.getName(), UserRoles.ADMIN.getName())

                        .pathMatchers("/user/get/uuid/")
                        .hasAnyRole(UserRoles.USER.getName(), UserRoles.ADMIN.getName())

                        .pathMatchers("/address/get/uuid/")
                        .hasAnyRole(UserRoles.USER.getName(), UserRoles.ADMIN.getName())

                        // Product API
                        .pathMatchers("/product/get/name/").permitAll()
                        .pathMatchers("/product/get/category/").permitAll()
                        .pathMatchers("/product/get/uuid/").permitAll()

                        .pathMatchers("/product/add")
                        .hasAnyRole(UserRoles.ADMIN.getName(), UserRoles.SYSTEM.getName())

                        .pathMatchers("/product/update")
                        .hasAnyRole(UserRoles.ADMIN.getName(), UserRoles.SYSTEM.getName())

                        .pathMatchers("/product/update/price")
                        .hasAnyRole(UserRoles.ADMIN.getName(), UserRoles.SYSTEM.getName())
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtDecoder(jwtDecoder()))
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jjwt -> {
                            Collection<GrantedAuthority> authorities = jjwt.getClaimAsStringList("authorities").stream()
                                    .map(SimpleGrantedAuthority::new)
                                    .collect(Collectors.toList());
                            return Mono.just(new JwtAuthenticationToken(jjwt, authorities));
                        }))
                );
        return http.build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        byte[] keyBytes = System.getenv("SECRET_KEY").getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = new SecretKeySpec(keyBytes, "HmacSHA512");
        return NimbusReactiveJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS512).build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/nonexistent/");
    }
}