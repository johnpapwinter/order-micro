package com.example.orders.service;

import com.example.orders.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    Long createOrder(OrderDTO dto);

    OrderDTO getOrder(Long id);

    OrderDTO updateOrder(Long id, OrderDTO dto);

    void deleteOrder(Long id);

    void processOrders();

    Page<OrderDTO> getOrders(Pageable pageable);

}
