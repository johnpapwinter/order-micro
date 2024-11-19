package com.example.orders.config;

import com.example.orders.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class Scheduler {

    @Value("${task.schedule.cron}")
    private String cronExpression;

    private final OrderService orderService;

    public Scheduler(OrderService orderService) {
        this.orderService = orderService;
    }

    @Scheduled(cron = "${task.schedule.cron}")
    public void processUnprocessedOrders() {
        orderService.processOrders();
    }


}
