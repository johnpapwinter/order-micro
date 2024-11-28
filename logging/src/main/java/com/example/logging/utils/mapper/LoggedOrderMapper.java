package com.example.logging.utils.mapper;

import com.example.logging.dto.LoggedOrderDTO;
import com.example.logging.model.LoggedOrder;
import org.springframework.stereotype.Component;

@Component
public class LoggedOrderMapper {

    public LoggedOrderDTO toLoggedOrderDTO(LoggedOrder loggedOrder) {
        return LoggedOrderDTO.builder()
                .id(loggedOrder.getId())
                .orderId(loggedOrder.getOrderId())
                .amount(loggedOrder.getAmount())
                .itemsCount(loggedOrder.getItemsCount())
                .date(loggedOrder.getDate())
                .build();
    }

    public LoggedOrder toLoggedOrder(LoggedOrderDTO loggedOrderDTO) {
        LoggedOrder loggedOrder = new LoggedOrder();
        loggedOrder.setOrderId(loggedOrderDTO.getOrderId());
        loggedOrder.setAmount(loggedOrderDTO.getAmount());
        loggedOrder.setItemsCount(loggedOrderDTO.getItemsCount());
        loggedOrder.setDate(loggedOrderDTO.getDate());
        return loggedOrder;
    }


}
