package ostro.veda.gateway_ms.security;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebFluxSecurity
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("inventory-service", r -> r.path("/api/v1/inventory/**")
                        .filters(f -> f.addRequestHeader("X-Forwarded-By", "Gateway"))
                        .uri("http://localhost:8083"))
                .route("product-service", r -> r.path("/api/v1/product/**")
                        .filters(f -> f.addRequestHeader("X-Forwarded-By", "Gateway"))
                        .uri("http://localhost:8081"))
                .route("order-service", r -> r.path("/api/v1/order/**")
                        .filters(f -> f.addRequestHeader("X-Forwarded-By", "Gateway"))
                        .uri("http://localhost:8082"))
                .route("user-service", r -> r.path("/user/login")
                        .uri("http://localhost:8084/api/v1/user/login"))
                .build();
    }

    @Bean
    protected SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(auth -> auth
                        .pathMatchers("/user/login").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtDecoder(jwtDecoder()))
                );
        return http.build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        byte[] keyBytes = System.getenv("SECRET_KEY").getBytes(); // Use a secure key in practice!
        javax.crypto.SecretKey secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
        return NimbusReactiveJwtDecoder.withSecretKey(secretKey).build();
    }
}