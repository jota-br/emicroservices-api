package io.github.jotabrc.service;

import io.github.jotabrc.model.Cache;
import io.github.jotabrc.security.SecurityHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

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

            String uri = "localhost:8084" + exchange.getRequest().getPath().toString();
            String key = uri.substring(uri.lastIndexOf("/") + 1);

            String cacheResponse = cacheService.getCache(key);
            if (cacheResponse != null) {
                exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");
                return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                        .bufferFactory().wrap(cacheResponse.getBytes())));
            }

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8084/api/v1/user/" + key)
                    .headers(header -> {
                        String data = Instant.now().toString();
                        header.add(HttpHeaders.AUTHORIZATION, authHeader);
                        header.add("X-Secure-Data", data);
                        try {
                            header.add("X-Secure-Origin", SecurityHeader.getEncryptedHeader(data));
                        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe(data -> {
                        Cache cache = new Cache(key, data);
                        System.out.println(data);
                        cacheService.setCache(cache);
                    });
            return chain.filter(exchange);
        });
    }
}

