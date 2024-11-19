package com.example.orders.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderLineDTO {

    private Long id;
    private String productId;
    private Integer quantity;
    private Double price;


}
