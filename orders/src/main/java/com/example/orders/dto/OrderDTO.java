package com.example.orders.dto;

import com.example.orders.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class OrderDTO {

    private Long id;
    private String orderId;
    private String customerName;
    private OrderStatus orderStatus;
    private LocalDate orderDate;
    private List<OrderLineDTO> orderLines;

}
