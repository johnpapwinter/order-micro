package com.example.orders.utils.mappers;

import com.example.orders.dto.OrderDTO;
import com.example.orders.enums.OrderStatus;
import com.example.orders.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderDTO toOrderDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .orderId(order.getOrderId())
                .customerName(order.getCustomerName())
                .orderStatus(order.getOrderStatus())
                .orderDate(order.getOrderDate())
                .build();
    }

    public Order toOrder(OrderDTO orderDTO) {
        Order order = new Order();
        order.setOrderId(orderDTO.getOrderId());
        order.setCustomerName(orderDTO.getCustomerName());
        order.setOrderStatus(OrderStatus.UNPROCESSED);
        order.setOrderDate(orderDTO.getOrderDate());
        return order;
    }

}
