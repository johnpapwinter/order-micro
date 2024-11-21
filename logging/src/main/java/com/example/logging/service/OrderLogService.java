package com.example.logging.service;

import com.example.logging.dto.OrderLogDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderLogService {

    void storeOrderLog(OrderLogDTO dto);

    Page<OrderLogDTO> getOrderLogs(Pageable pageable);

}
