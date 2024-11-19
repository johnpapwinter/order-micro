package com.example.orders.service;

import com.example.orders.dto.OrderLineDTO;
import com.example.orders.model.Order;
import com.example.orders.model.OrderLine;
import com.example.orders.repository.OrderLineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderLineServiceImpl implements OrderLineService {

    private final OrderLineRepository orderLineRepository;

    public OrderLineServiceImpl(OrderLineRepository orderLineRepository) {
        this.orderLineRepository = orderLineRepository;
    }

    @Override
    @Transactional
    public Long createOrderLine(OrderLineDTO dto, Order order) {
        return 0L;
    }

    @Override
    @Transactional
    public OrderLine updateOrderLine(OrderLineDTO dto, Order order) {
        return null;
    }

    @Override
    @Transactional
    public void deleteOrderLine(Long id) {

    }
}
