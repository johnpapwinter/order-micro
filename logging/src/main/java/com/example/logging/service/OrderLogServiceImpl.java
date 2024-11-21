package com.example.logging.service;

import com.example.logging.dto.OrderLogDTO;
import com.example.logging.model.OrderLog;
import com.example.logging.repository.OrderLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderLogServiceImpl implements OrderLogService {

    private final OrderLogRepository orderLogRepository;

    public OrderLogServiceImpl(OrderLogRepository orderLogRepository) {
        this.orderLogRepository = orderLogRepository;
    }

    @Override
    @Transactional
    public void storeOrderLog(OrderLogDTO dto) {
        OrderLog orderLog = new OrderLog();
        orderLog.setTimestamp(dto.getTimestamp());
        orderLog.setThread(dto.getThread());
        orderLog.setLevel(dto.getLevel());
        orderLog.setLogger(dto.getLogger());
        orderLog.setMessage(dto.getMessage());

        orderLogRepository.save(orderLog);
    }

}
