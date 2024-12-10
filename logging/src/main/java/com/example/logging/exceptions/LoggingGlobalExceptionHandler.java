package com.example.logging.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class LoggingGlobalExceptionHandler {

    Logger LOGGER = LoggerFactory.getLogger(LoggingGlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageDTO> handleException(Exception e) {
        LOGGER.error("Error processing request: {}", e.getMessage());
        ErrorMessageDTO messageDTO = ErrorMessageDTO.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(ErrorMessages.CONTACT_YOUR_ADMINISTRATOR)
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(messageDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
