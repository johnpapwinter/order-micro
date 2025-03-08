package com.example.orders.config;

import com.example.orders.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;

@Configuration
@Slf4j
public class Scheduler {

    private final OrderService orderService;
    private final RedisTemplate<String, String> redisTemplate;

    public Scheduler(OrderService orderService, RedisTemplate<String, String> redisTemplate) {
        this.orderService = orderService;
        this.redisTemplate = redisTemplate;
    }

    @Scheduled(cron = "${task.schedule.cron}")
    public void processUnprocessedOrders() {
        // try to acquire distributed lock with expiration
        log.info("Acquiring lock for unprocessed orders");
        Boolean lockAcquired = redisTemplate.opsForValue()
                .setIfAbsent("order-processing-lock", "locked", Duration.ofMinutes(2));
        log.info("Lock acquired: {}", lockAcquired);

        if (Boolean.TRUE.equals(lockAcquired)) {
            try {
                orderService.processOrders();
            }
            finally {
                // release lock when done
                redisTemplate.delete("order-processing-lock");
                log.info("Lock for unprocessed orders released");
            }
        }
    }


}
