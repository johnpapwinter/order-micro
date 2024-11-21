package com.example.logging.service;

import com.example.logging.dto.OrderLogDTO;

public interface OrderLogService {

    void storeOrderLog(OrderLogDTO dto);

}
