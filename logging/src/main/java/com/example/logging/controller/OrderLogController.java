package com.example.logging.controller;

import com.example.logging.dto.OrderLogDTO;
import com.example.logging.service.OrderLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logs")
public class OrderLogController {

    private final OrderLogService orderLogService;

    public OrderLogController(OrderLogService orderLogService) {
        this.orderLogService = orderLogService;
    }

    @PostMapping
    public ResponseEntity<Void> createLog(@RequestBody OrderLogDTO dto) {
        orderLogService.storeOrderLog(dto);

        return ResponseEntity.ok().build();
    }

}
