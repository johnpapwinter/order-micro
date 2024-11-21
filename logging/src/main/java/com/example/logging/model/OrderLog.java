package com.example.logging.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collation = "order_logs")
public class OrderLog {

    @Id
    private String id;

    private Instant timestamp;
    private String thread;
    private String level;
    private String logger;
    private String message;


}
