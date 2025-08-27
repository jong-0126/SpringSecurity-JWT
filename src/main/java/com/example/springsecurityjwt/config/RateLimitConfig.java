package com.example.springsecurityjwt.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@RequiredArgsConstructor
public class RateLimitConfig {

    // ip 주소 또는 사용자 ID 등을 key로 하고, Bucket 객체를 value로 가지는 메모리 기반 저장소
    // ConcurrentHashMap으로 멀티스레드 환경에서도 안전하게 Bucket을 저장하고 조회하기 위해 사용
    private final Map<String, Bucket> bucketCache = new ConcurrentHashMap<>();

    // 클라이언트 ip로 버킷을 가져오거나, 없으면 새로 생성
    public Bucket resolveBucket(String ip) {
        // 키가 없을 때만 newBucket(ip) 호출하여 생성 -> 중복 생성 방지
        return bucketCache.computeIfAbsent(ip, this::newBucket);
    }

    // 새로운 버킷 생성 빌더 시작
    private Bucket newBucket(String key) {
        return Bucket4j.builder()
                // 총 10개 저장 가능, 1분 마다 10개 토큰 재충전 즉, 1분에 10번 요청 허용됨
                .addLimit(Bandwidth.classic(10, Refill.intervally(10, Duration.ofMinutes(1))))
                .build();
    }
}
