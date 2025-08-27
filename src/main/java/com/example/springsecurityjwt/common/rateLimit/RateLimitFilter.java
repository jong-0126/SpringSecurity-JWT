package com.example.springsecurityjwt.common.rateLimit;

import com.example.springsecurityjwt.config.RateLimitConfig;
import io.github.bucket4j.Bucket;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    // 요청마다 ip 별 Bucket을 꺼내기 위해 사용
    private final RateLimitConfig rateLimitConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 요청자의 ip 주소를 가져옴
        String ip = request.getRemoteAddr();
        // ip에 대한 요청 제한 버킷을 가져옴
        Bucket bucket = rateLimitConfig.resolveBucket(ip);

        // 1개의 요청 허용량을 사용 시도, 가능하면 다음 필터로 넘어감
        if(bucket.tryConsume(1)){
            filterChain.doFilter(request, response);
        } else{
            // 실패하면 429 Too Many Requests 에러 응답 반환
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value()); // 429
            response.getWriter().write("Too many requests");
        }
    }
}
