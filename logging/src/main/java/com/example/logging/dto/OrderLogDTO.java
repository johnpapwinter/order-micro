package com.example.logging.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class OrderLogDTO {

    private String id;
    private Instant timestamp;
    private String thread;
    private String level;
    private String logger;
    private String message;


}
