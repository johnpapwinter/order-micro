package com.example.logging.controller;

import com.example.logging.dto.LoggedOrderDTO;
import com.example.logging.service.LoggedOrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logs")
public class LoggedOrderController {

    private final LoggedOrderService loggedOrderService;

    public LoggedOrderController(LoggedOrderService loggedOrderService) {
        this.loggedOrderService = loggedOrderService;
    }


    @PostMapping
    public ResponseEntity<Void> saveLoggedOrder(@RequestBody LoggedOrderDTO dto) {
        loggedOrderService.createLoggedOrder(dto);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<LoggedOrderDTO>> getLoggedOrders(Pageable pageable) {
        Page<LoggedOrderDTO> response = loggedOrderService.getAllLoggedOrders(pageable);

        return ResponseEntity.ok().body(response);
    }

}
