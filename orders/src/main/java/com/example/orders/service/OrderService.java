package com.example.orders.service;

import com.example.orders.dto.OrderDTO;

public interface OrderService {

    Long createOrder(OrderDTO dto);

    OrderDTO getOrder(Long id);

    OrderDTO updateOrder(OrderDTO dto);

    void deleteOrder(Long id);

    void processOrders();

}
