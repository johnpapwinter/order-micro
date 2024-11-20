package com.example.orders.exception;

public class DataMismatchException extends RuntimeException {
    public DataMismatchException(String message) {
        super(message);
    }
}
