package com.example.logging.exceptions;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorMessageDTO {

    private HttpStatus httpStatus;
    private String message;
    private LocalDateTime timestamp;

}
