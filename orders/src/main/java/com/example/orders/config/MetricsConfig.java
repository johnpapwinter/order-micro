package com.example.orders.config;

import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public Timer orderProcessingTimer(MeterRegistry registry) {
        return Timer.builder("orders.processing.time")
                .description("Time taken to process orders")
                .register(registry);
    }

    @Bean
    public DistributionSummary orderValueSummary(MeterRegistry registry) {
        return DistributionSummary.builder("orders.value")
                .description("Distribution of order values")
                .baseUnit("EUR")
                .register(registry);
    }


}
