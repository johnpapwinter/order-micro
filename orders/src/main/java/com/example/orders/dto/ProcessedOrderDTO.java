package com.example.orders.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ProcessedOrderDTO {

    private String orderId;
    private Double amount;
    private Integer itemsCount;
    private LocalDate date;

}
