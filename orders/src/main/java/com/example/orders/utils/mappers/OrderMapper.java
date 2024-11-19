package com.example.orders.utils.mappers;

import com.example.orders.dto.OrderDTO;
import com.example.orders.enums.OrderStatus;
import com.example.orders.model.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderMapper {

    private final OrderLineMapper orderLineMapper;

    public OrderMapper(OrderLineMapper orderLineMapper) {
        this.orderLineMapper = orderLineMapper;
    }


    public OrderDTO toOrderDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .orderId(order.getOrderId())
                .customerName(order.getCustomerName())
                .orderStatus(order.getOrderStatus())
                .orderDate(order.getOrderDate())
                .orderLines(order.getOrderLines().stream()
                        .map(orderLineMapper::toOrderLineDTO)
                        .toList())
                .build();
    }

    public Order toOrder(OrderDTO orderDTO) {
        Order order = new Order();
        order.setId(orderDTO.getId());
        order.setOrderId(orderDTO.getOrderId());
        order.setCustomerName(orderDTO.getCustomerName());
        Optional.ofNullable(orderDTO.getId()).ifPresentOrElse(
                id -> order.setOrderStatus(orderDTO.getOrderStatus()),
                () -> order.setOrderStatus(OrderStatus.UNPROCESSED));
        order.setOrderDate(orderDTO.getOrderDate());
        return order;
    }

}
