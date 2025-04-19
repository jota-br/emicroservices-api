package io.github.jotabrc.service;

import io.github.jotabrc.model.Cache;
import io.github.jotabrc.repository.CacheRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CacheServiceImpl implements CacheService {

    private final CacheRepository cacheRepository;

    public CacheServiceImpl(CacheRepository cacheRepository) {
        this.cacheRepository = cacheRepository;
    }

    @Override
    public void setCache(final Cache cache) {
        cacheRepository.save(cache);
    }

    @Override
    public String getCache(String key) {
        Optional<Cache> result = cacheRepository.findById(key);
        return result.map(Cache::getValue).orElse(null);
    }
}
