package ostro.veda.gateway_ms.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("inventory-service", r -> r.path("/api/v1/inventory/**")
                        .uri("http://localhost:8083"))
                .route("product-service", r -> r.path("/api/v1/product/**")
                        .uri("http://localhost:8081"))
                .route("order-service", r -> r.path("/api/v1/order/**")
                        .uri("http://localhost:8082"))
                .build();
    }
}
