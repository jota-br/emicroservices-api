package ostro.veda.gateway_ms.security;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

class AuthorizationHeaderFilter implements GatewayFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header("Authorization", token)
                    .build();

            exchange = exchange.mutate().request(modifiedRequest).build();
        }
        return chain.filter(exchange);
    }
}
