package io.github.jotabrc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class CacheService {

    private final ReactiveRedisOperations<String, String> redisOperations;

    @Autowired
    public CacheService(ReactiveRedisOperations<String, String> redisOperations) {
        this.redisOperations = redisOperations;
    }

    public Mono<String> getCache(String key) {
        return redisOperations.opsForValue().get(key);
    }

    public Mono<Boolean> saveToCache(String key, String value, long expiration) {
        return redisOperations.opsForValue().set(key, value).map(result -> {
            redisOperations.expire(key, Duration.ofSeconds(expiration)).subscribe();
            return result;
        });
    }
}
