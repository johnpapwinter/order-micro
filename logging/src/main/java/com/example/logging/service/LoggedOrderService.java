package com.example.logging.service;

import com.example.logging.dto.LoggedOrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LoggedOrderService {

    void createLoggedOrder(LoggedOrderDTO dto);

    Page<LoggedOrderDTO> getAllLoggedOrders(Pageable pageable);

}
