//package ostro.veda.gateway_ms.security;
//
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//class CustomHeaderFilter implements GatewayFilter {
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
//                .header("X-Custom-Header", "CustomValue")
//                .build();
//        exchange = exchange.mutate().request(modifiedRequest).build();
//        return chain.filter(exchange);
//    }
//}
