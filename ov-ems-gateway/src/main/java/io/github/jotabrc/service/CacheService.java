package io.github.jotabrc.service;

import io.github.jotabrc.model.Cache;

public interface CacheService {

    void setCache(Cache cache);

    String getCache(String key);
}
