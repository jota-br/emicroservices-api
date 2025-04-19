package io.github.jotabrc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash(value = "Cache", timeToLive = 1L)
@Getter
@Setter
@AllArgsConstructor
public class Cache implements Serializable {

    private String id;
    private String value;
}
