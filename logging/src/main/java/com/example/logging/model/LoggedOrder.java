package com.example.logging.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "logged_orders")
@Getter
@Setter
public class LoggedOrder {

    @Id
    private String id;

    private String orderId;
    private Double amount;
    private Integer itemsCount;
    private LocalDate date;


}
