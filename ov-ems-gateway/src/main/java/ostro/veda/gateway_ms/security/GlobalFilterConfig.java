package ostro.veda.gateway_ms.security;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

@Component
public class GlobalFilterConfig implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && !authHeader.startsWith("Bearer ")) {
            authHeader = null;
        }

        ServerHttpRequest modifiedRequest = null;
        try {
            String data = Instant.now().toString();
            modifiedRequest = exchange.getRequest().mutate()
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .header("X-Secure-Data", Instant.now().toString())
                    .header("X-Secure-Origin", SecurityHeader.getEncryptedHeader(data))
                    .build();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        exchange = exchange.mutate().request(modifiedRequest).build();
        return chain.filter(exchange);
    }
}
