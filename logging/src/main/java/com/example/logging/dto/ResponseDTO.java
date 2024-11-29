package com.example.logging.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ResponseDTO implements Serializable {

    private String orderId;
    private boolean success;
    private String message;

    public ResponseDTO(@JsonProperty("orderId") String orderId,
                       @JsonProperty("success") boolean success,
                       @JsonProperty("message") String message) {
        this.orderId = orderId;
        this.success = success;
        this.message = message;
    }

}
