package com.example.orders.dto;

import lombok.Data;

@Data
public class OrderLineDTO {

    private Long id;
    private String productId;
    private Integer quantity;
    private Double price;


}
