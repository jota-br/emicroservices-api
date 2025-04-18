package io.github.jotabrc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class RedisCacheFilterFactory extends AbstractGatewayFilterFactory<RedisCacheFilterConfig> {

    private final CacheService cacheService;
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public RedisCacheFilterFactory(CacheService cacheService, WebClient.Builder webClientBuilder) {
        this.cacheService = cacheService;
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(RedisCacheFilterConfig redisCacheFilterConfig) {
        return ((exchange, chain) -> {
            if (!exchange.getRequest().getMethod().equals(HttpMethod.GET)) return chain.filter(exchange);

            String requestedKey = exchange.getRequest().getPath().toString();

            return cacheService.getCache(requestedKey)
                    .flatMap(cacheResponse -> {
                        if (cacheResponse != null) {
                            exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");
                            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                                    .bufferFactory().wrap(cacheResponse.getBytes())));
                        } else {
                            return webClientBuilder.build()
                                    .get()
                                    .uri(exchange.getRequest().getPath().value())
                                    .retrieve()
                                    .bodyToMono(String.class)
                                    .flatMap(serviceResponse -> {
                                        cacheService.saveToCache(requestedKey, serviceResponse, redisCacheFilterConfig.getExpiration());
                                        exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");
                                        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                                                .bufferFactory().wrap(serviceResponse.getBytes())));
                                    });
                        }
                    });
        });
    }
}

