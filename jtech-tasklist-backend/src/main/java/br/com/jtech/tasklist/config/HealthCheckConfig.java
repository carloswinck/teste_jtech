package br.com.jtech.tasklist.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class HealthCheckConfig implements HealthIndicator {

    @Override
    public Health health() {
        Map<String, Object> details = new HashMap<>();
        details.put("status", "UP");
        details.put("service", "JTech TaskList API");
        details.put("version", "1.0.0");
        details.put("timestamp", LocalDateTime.now());
        details.put("environment", "development");
        details.put("database", "PostgreSQL");

        return Health.up()
                .withDetails(details)
                .build();
    }
}
