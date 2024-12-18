package com.example.orders.security.dto;

import com.example.orders.exception.ErrorMessages;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {

    @NotBlank(message = ErrorMessages.USERNAME_CANNOT_BE_EMPTY)
    private String username;
    @NotBlank(message = ErrorMessages.PASSWORD_CANNOT_BE_EMPTY)
    private String password;

}
