package com.example.logging.service;

import com.example.logging.dto.OrderLogDTO;
import com.example.logging.model.OrderLog;
import com.example.logging.repository.OrderLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
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

    @Override
    @Transactional(readOnly = true)
    public Page<OrderLogDTO> getOrderLogs(Pageable pageable) {
        return orderLogRepository.findAll(pageable)
                .map(orderLog -> {
                    OrderLogDTO dto = new OrderLogDTO();
                    dto.setId(orderLog.getId());
                    dto.setTimestamp(orderLog.getTimestamp());
                    dto.setThread(orderLog.getThread());
                    dto.setLevel(orderLog.getLevel());
                    dto.setLogger(orderLog.getLogger());
                    dto.setMessage(orderLog.getMessage());
                    return dto;
                });
    }
}
