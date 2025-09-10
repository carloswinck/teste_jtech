package br.com.jtech.tasklist.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

@TestConfiguration
@ActiveProfiles("test")
public class TestConfig {

    @Bean
    @Primary
    public TestDataBuilder testDataBuilder() {
        return new TestDataBuilder();
    }
}
