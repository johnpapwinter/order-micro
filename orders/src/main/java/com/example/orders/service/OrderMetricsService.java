package com.example.orders.service;

import com.example.orders.enums.OrderStatus;
import com.example.orders.repository.OrderRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderMetricsService {

    private final OrderRepository orderRepository;
    private final MeterRegistry meterRegistry;

    public OrderMetricsService(OrderRepository orderRepository, MeterRegistry meterRegistry) {
        this.orderRepository = orderRepository;
        this.meterRegistry = meterRegistry;

        Gauge.builder("orders.monthly.status", this, OrderMetricsService::getProcessedOrdersLastMonth)
                .tag("status", "PROCESSED")
                .description("Number of processed orders in the last month")
                .register(meterRegistry);

        Gauge.builder("orders.monthly.status", this, OrderMetricsService::getUnprocessedOrdersLastMonth)
                .tag("status", "UNPROCESSED")
                .description("Number of unprocessed orders in the last month")
                .register(meterRegistry);
    }

    private double getProcessedOrdersLastMonth() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        return orderRepository.countByOrderStatusAndCreatedDateAfter(OrderStatus.PROCESSED, oneMonthAgo);
    }

    private double getUnprocessedOrdersLastMonth() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        return orderRepository.countByOrderStatusAndCreatedDateAfter(OrderStatus.UNPROCESSED, oneMonthAgo);
    }


}
