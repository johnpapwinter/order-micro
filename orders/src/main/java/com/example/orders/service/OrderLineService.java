package com.example.orders.service;

import com.example.orders.dto.OrderLineDTO;
import com.example.orders.model.Order;
import com.example.orders.model.OrderLine;

import java.util.List;

public interface OrderLineService {

    Long createOrderLine(OrderLineDTO dto, Order order);

    OrderLine updateOrderLine(OrderLineDTO dto, Order order);

    void deleteOrderLine(Long id);

    void deleteOrderLines(List<OrderLine> orderLines);

}
