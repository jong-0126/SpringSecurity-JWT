package com.example.springsecurityjwt.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class RateLimitConfig {

    private final Map<String, Bucket> bucketCache = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String username, String role) {
        String key = username + ":" + role;

        return bucketCache.computeIfAbsent(key, k -> {
            if ("ROLE_ADMIN".equals(role)) {
                return newBucket(100); // 관리자: 분당 100회
            } else if ("ROLE_USER".equals(role)) {
                return newBucket(10);  // 일반 사용자: 분당 10회
            } else {
                return newBucket(5);   // 그 외
            }
        });
    }

    private Bucket newBucket(int capacity) {
        return Bucket4j.builder()
                .addLimit(Bandwidth.classic(capacity, Refill.intervally(capacity, Duration.ofMinutes(1))))
                .build();
    }
}

