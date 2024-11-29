package com.example.logging.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
public class LoggedOrderDTO implements Serializable {

    @JsonProperty("id")
    private String id;
    @JsonProperty("orderId")
    private String orderId;
    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("itemsCount")
    private Integer itemsCount;
    @JsonProperty("date")
    private LocalDate date;


}
