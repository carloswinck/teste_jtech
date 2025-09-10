package br.com.jtech.tasklist.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long startTime = System.currentTimeMillis();
        String requestId = UUID.randomUUID().toString();

        request.setAttribute("startTime", startTime);
        request.setAttribute("requestId", requestId);

        logger.info("Incoming request [{}]: {} {} from {}",
                requestId,
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long startTime = (Long) request.getAttribute("startTime");
        String requestId = (String) request.getAttribute("requestId");
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        if (ex != null) {
            logger.error("Request failed [{}]: {} {} - Status: {} - Duration: {}ms - Error: {}",
                    requestId,
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    duration,
                    ex.getMessage());
        } else {
            logger.info("Request completed [{}]: {} {} - Status: {} - Duration: {}ms",
                    requestId,
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    duration);
        }
    }
}
