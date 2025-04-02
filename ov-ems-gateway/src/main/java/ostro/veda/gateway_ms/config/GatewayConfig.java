package ostro.veda.gateway_ms.config;

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

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r.path("/user/**")
                        .filters(f -> f
                                .rewritePath("/user/(?<segment>.*)", "/api/v1/user/${segment}")
                        )
                        .uri("http://localhost:8084"))
                .build();
    }

    @Bean
    protected SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(auth -> auth
                        .pathMatchers("/user/login").permitAll()
                        .pathMatchers("/user/add").permitAll()
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