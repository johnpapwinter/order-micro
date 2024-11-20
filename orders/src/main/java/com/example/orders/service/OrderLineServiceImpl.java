package com.example.orders.service;

import com.example.orders.dto.OrderLineDTO;
import com.example.orders.model.Order;
import com.example.orders.model.OrderLine;
import com.example.orders.repository.OrderLineRepository;
import com.example.orders.utils.mappers.OrderLineMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderLineServiceImpl implements OrderLineService {

    private final OrderLineRepository orderLineRepository;
    private final OrderLineMapper orderLineMapper;

    public OrderLineServiceImpl(OrderLineRepository orderLineRepository,
                                OrderLineMapper orderLineMapper) {
        this.orderLineRepository = orderLineRepository;
        this.orderLineMapper = orderLineMapper;
    }

    @Override
    @Transactional
    public void createOrderLine(OrderLineDTO dto, Order order) {
        OrderLine orderLine = orderLineMapper.toOrderLine(dto);
        orderLine.setOrder(order);
        order.getOrderLines().add(orderLine);
    }

    @Override
    @Transactional
    public void updateOrderLine(OrderLineDTO dto, OrderLine orderLine) {
        // ONLY THE QUANTITY AND THE PRICE ARE UPDATABLE
        orderLine.setQuantity(dto.getQuantity());
        orderLine.setPrice(dto.getPrice());
    }

    @Override
    public void deleteOrderLines(List<OrderLine> orderLines) {
        orderLineRepository.deleteAll(orderLines);
    }

}
