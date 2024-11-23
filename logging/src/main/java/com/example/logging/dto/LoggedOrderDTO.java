package com.example.logging.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class LoggedOrderDTO {

    private String id;
    private String orderId;
    private Double amount;
    private Integer itemsCount;
    private LocalDate date;


}
