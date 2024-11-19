package com.example.orders.utils.mappers;

import com.example.orders.dto.OrderLineDTO;
import com.example.orders.model.OrderLine;
import org.springframework.stereotype.Component;

@Component
public class OrderLineMapper {

    public OrderLineDTO toOrderLineDTO(OrderLine orderLine) {
        return OrderLineDTO.builder()
                .id(orderLine.getId())
                .productId(orderLine.getProductId())
                .quantity(orderLine.getQuantity())
                .price(orderLine.getPrice())
                .build();
    }

    public OrderLine toOrderLine(OrderLineDTO orderLineDTO) {
        OrderLine orderLine = new OrderLine();
        orderLine.setId(orderLineDTO.getId());
        orderLine.setProductId(orderLineDTO.getProductId());
        orderLine.setQuantity(orderLineDTO.getQuantity());
        orderLine.setPrice(orderLineDTO.getPrice());
        return orderLine;
    }

}
