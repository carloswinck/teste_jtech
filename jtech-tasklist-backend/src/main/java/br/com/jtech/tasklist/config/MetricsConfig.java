package br.com.jtech.tasklist.config;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }

    @Bean
    public Counter taskCreatedCounter(MeterRegistry registry) {
        return Counter.builder("tasklist.tasks.created")
                .description("Number of tasks created")
                .register(registry);
    }

    @Bean
    public Counter tasklistCreatedCounter(MeterRegistry registry) {
        return Counter.builder("tasklist.tasklists.created")
                .description("Number of tasklists created")
                .register(registry);
    }
}
