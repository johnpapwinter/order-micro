package com.example.orders.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class LogMessageDTO {

    private Instant timestamp;
    private String thread;
    private String level;
    private String logger;
    private String message;

}
