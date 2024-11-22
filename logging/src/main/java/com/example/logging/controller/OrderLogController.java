package com.example.logging.controller;

import com.example.logging.dto.OrderLogDTO;
import com.example.logging.service.OrderLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/logs")
@Slf4j
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

    @GetMapping
    public ResponseEntity<Page<OrderLogDTO>> getAllLogs(Pageable pageable) {
        Page<OrderLogDTO> response = orderLogService.getOrderLogs(pageable);

        return ResponseEntity.ok(response);
    }


}
