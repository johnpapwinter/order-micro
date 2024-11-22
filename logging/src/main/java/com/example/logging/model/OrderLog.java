package com.example.logging.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "order_logs")
@Getter
@Setter
public class OrderLog {

    @Id
    private String id;

    private Instant timestamp;
    private String thread;
    private String level;
    private String logger;
    private String message;


}
