package io.github.jotabrc.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class RedisCacheFilterConfig {
    private long expiration = 300;
}
