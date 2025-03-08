package com.example.orders.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class OrdersGlobalExceptionHandler {

    Logger LOGGER = LoggerFactory.getLogger(OrdersGlobalExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessageDTO> handleEntityNotFoundException(EntityNotFoundException e) {
        LOGGER.error("Entity already exists: {}", e.getMessage());
        ErrorMessageDTO messageDTO = ErrorMessageDTO.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(messageDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataMismatchException.class)
    public ResponseEntity<ErrorMessageDTO> handleDataMismatchException(DataMismatchException e) {
        LOGGER.error("Data mismatch: {}", e.getMessage());
        ErrorMessageDTO messageDTO = ErrorMessageDTO.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(messageDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorMessageDTO> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        LOGGER.error("Data Integrity: {}", e.getMessage());
        ErrorMessageDTO messageDTO = ErrorMessageDTO.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message(ErrorMessages.DATA_CONSTRAINT_VIOLATION)
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(messageDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LoggingServiceException.class)
    public ResponseEntity<ErrorMessageDTO> handleLoggingServiceException(LoggingServiceException e) {
        LOGGER.error("Logging service error: {}", e.getMessage());
        ErrorMessageDTO messageDTO = ErrorMessageDTO.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(messageDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationExceptions(MethodArgumentNotValidException e) {
        LOGGER.error("Invalid arguments: {}", e.getMessage());
        List<String> errors = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageDTO> handleException(Exception e) {
        LOGGER.error("Generic error: {}", e.getMessage());
        e.printStackTrace();
        ErrorMessageDTO messageDTO = ErrorMessageDTO.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(ErrorMessages.CONTACT_YOUR_ADMINISTRATOR)
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(messageDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
