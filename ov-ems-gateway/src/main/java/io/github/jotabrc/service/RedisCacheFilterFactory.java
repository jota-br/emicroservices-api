package io.github.jotabrc.service;

import io.github.jotabrc.config.ServiceConfiguration;
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
    private final ServiceConfiguration serviceConfig;

    @Autowired
    public RedisCacheFilterFactory(CacheService cacheService, WebClient.Builder webClientBuilder, ServiceConfiguration serviceConfig) {
        this.cacheService = cacheService;
        this.webClientBuilder = webClientBuilder;
        this.serviceConfig = serviceConfig;
    }

    @Override
    public GatewayFilter apply(RedisCacheFilterConfig redisCacheFilterConfig) {
        return ((exchange, chain) -> {

            String service = exchange
                    .getAttribute("org.springframework.cloud.gateway.support.ServerWebExchangeUtils.gatewayPredicateMatchedPathAttr");
            if (service == null || !exchange.getRequest().getMethod().equals(HttpMethod.GET)) return chain.filter(exchange);

            String path = exchange.getRequest().getPath().toString();
            String key = path.substring(path.lastIndexOf("/") + 1);

            String cacheResponse = cacheService.getCache(key);
            if (cacheResponse != null) {
                exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");
                return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                        .bufferFactory().wrap(cacheResponse.getBytes())));
            }

            // JWT
            String jwt = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            StringBuilder uri = new StringBuilder();
            switch (service) {
                case "/user/**", "/activation-token/**" -> uri.append(serviceConfig.getUserServiceUri());
                case "/inventory/**" -> uri.append(serviceConfig.getInventoryServiceUri());
                case "/order/**" -> uri.append(serviceConfig.getOrderServiceUri());
                case "/product/**" -> uri.append(serviceConfig.getProductServiceUri());
            }

            if (uri.isEmpty()) return chain.filter(exchange);

            uri
                    .append(path);

            webClientBuilder.build()
                    .get()
                    .uri(uri.toString())
                    .headers(header -> {
                        String data = Instant.now().toString();
                        header.add(HttpHeaders.AUTHORIZATION, jwt);
                        header.add("X-Secure-Data", data); // Data
                        try {
                            // Encrypted data for secure origin verification
                            header.add("X-Secure-Origin", SecurityHeader.getEncryptedHeader(data));
                        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe(data -> {
                        Cache cache = new Cache(key, data);
                        cacheService.setCache(cache);
                    });
            return chain.filter(exchange);
        });
    }
}

