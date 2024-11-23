package com.example.logging.service;

import com.example.logging.dto.LoggedOrderDTO;
import com.example.logging.model.LoggedOrder;
import com.example.logging.repository.LoggedOrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoggedOrderServiceImpl implements LoggedOrderService {

    private final LoggedOrderRepository loggedOrderRepository;

    public LoggedOrderServiceImpl(LoggedOrderRepository loggedOrderRepository) {
        this.loggedOrderRepository = loggedOrderRepository;
    }

    @Override
    @Transactional
    public void createLoggedOrder(LoggedOrderDTO dto) {
        LoggedOrder loggedOrder = new LoggedOrder();
        loggedOrder.setOrderId(dto.getOrderId());
        loggedOrder.setAmount(dto.getAmount());
        loggedOrder.setItemsCount(dto.getItemsCount());
        loggedOrder.setDate(dto.getDate());

        loggedOrderRepository.save(loggedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoggedOrderDTO> getAllLoggedOrders(Pageable pageable) {
        return loggedOrderRepository.findAll(pageable)
                .map(loggedOrder ->
                    LoggedOrderDTO.builder()
                            .id(loggedOrder.getId())
                            .orderId(loggedOrder.getOrderId())
                            .amount(loggedOrder.getAmount())
                            .itemsCount(loggedOrder.getItemsCount())
                            .date(loggedOrder.getDate())
                            .build()
                );
    }
}
