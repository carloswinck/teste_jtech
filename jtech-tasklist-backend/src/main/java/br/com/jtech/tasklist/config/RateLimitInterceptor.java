package br.com.jtech.tasklist.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final ConcurrentHashMap<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    private final int MAX_REQUESTS_PER_MINUTE = 100;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientId = getClientId(request);
        String endpoint = request.getRequestURI();

        String key = clientId + ":" + endpoint;
        AtomicInteger count = requestCounts.computeIfAbsent(key, k -> new AtomicInteger(0));

        if (count.incrementAndGet() > MAX_REQUESTS_PER_MINUTE) {
            response.setStatus(429); // HTTP 429 Too Many Requests
            response.setHeader("Retry-After", "60");
            response.getWriter().write("{\"error\": \"Rate limit exceeded\", \"retryAfter\": 60}");
            return false;
        }

        return true;
    }

    private String getClientId(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
