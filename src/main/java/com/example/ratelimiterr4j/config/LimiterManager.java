package com.example.ratelimiterr4j.config;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;

@Component
public class LimiterManager {

    @Autowired
    private RateLimiterRegistry rateLimiterRegistry;

    final ConcurrentMap<String, RateLimiter> keyRateLimiters =
            new ConcurrentHashMap<String, RateLimiter>();

    public Optional<RateLimiter> getRate() {
        return rateLimiterRegistry.find("customRateLimiterB");
    }
}
