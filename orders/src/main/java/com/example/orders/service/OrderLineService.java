package com.example.orders.service;

import com.example.orders.dto.OrderLineDTO;
import com.example.orders.model.Order;
import com.example.orders.model.OrderLine;

import java.util.List;

public interface OrderLineService {

    void createOrderLine(OrderLineDTO dto, Order order);

    void updateOrderLine(OrderLineDTO dto, OrderLine orderLine);

    void deleteOrderLines(List<OrderLine> orderLines);

}
