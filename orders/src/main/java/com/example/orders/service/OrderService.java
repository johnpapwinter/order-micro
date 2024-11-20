package com.example.orders.service;

import com.example.orders.dto.OrderDTO;

public interface OrderService {

    Long createOrder(OrderDTO dto);

    OrderDTO getOrder(Long id);

    OrderDTO updateOrder(Long id, OrderDTO dto);

    void deleteOrder(Long id);

    void processOrders();

}
