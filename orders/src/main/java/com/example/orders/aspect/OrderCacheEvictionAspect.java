package com.example.orders.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class OrderCacheEvictionAspect {

    @CacheEvict(value = "ordersPage", allEntries = true)
    @AfterReturning("execution(* com.example.orders.service.OrderService.createOrder(..)) || " +
            "execution(* com.example.orders.service.OrderService.updateOrder(..)) || " +
            "execution(* com.example.orders.service.OrderService.deleteOrder(..))")
    public void evictOrderPagesCache() {
        // The method is empty
        // The @CacheEvict annotation does the actual work
    }

}
