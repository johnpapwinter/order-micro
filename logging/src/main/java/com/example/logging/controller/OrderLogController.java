package com.example.logging.controller;

import com.example.logging.dto.OrderLogDTO;
import com.example.logging.service.OrderLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

    @GetMapping
    public ResponseEntity<Page<OrderLogDTO>> getAllLogs(Pageable pageable) {
        Page<OrderLogDTO> response = orderLogService.getOrderLogs(pageable);

        return ResponseEntity.ok(response);
    }


}
