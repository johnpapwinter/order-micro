package com.example.orders.controller;

import com.example.orders.dto.OrderDTO;
import com.example.orders.service.OrderService;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController implements OrderApi {

    private final OrderService orderService;
    private final MeterRegistry meterRegistry;


    @PostMapping
    public ResponseEntity<Long> createOrder(@RequestBody @Valid OrderDTO dto) {
        meterRegistry.counter("http.requests", "endpoint", "/api/orders", "method", "POST").increment();
        Long id = orderService.createOrder(dto);

        return ResponseEntity.ok(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long id) {
        meterRegistry.counter("http.requests", "endpoint", "/api/orders/{id}", "method", "GET").increment();
        OrderDTO dto = orderService.getOrder(id);

        return ResponseEntity.ok(dto);
    }

    @PutMapping
    public ResponseEntity<OrderDTO> updateOrder(@RequestBody @Valid OrderDTO dto) {
        meterRegistry.counter("http.requests", "endpoint", "/api/orders", "method", "PUT").increment();
        OrderDTO updatedDto = orderService.updateOrder(dto);

        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        meterRegistry.counter("http.requests", "endpoint", "/api/orders/{id}", "method", "DELETE").increment();
        orderService.deleteOrder(id);

        return ResponseEntity.noContent().build();
    }

}
