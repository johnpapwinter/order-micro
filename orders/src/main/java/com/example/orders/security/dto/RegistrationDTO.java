package com.example.orders.security.dto;

import com.example.orders.exception.ErrorMessages;
import com.example.orders.utils.annotations.PasswordMatch;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@PasswordMatch(
        password = "password",
        confirmPassword = "confirmPassword"
)
public class RegistrationDTO {

    @NotBlank(message = ErrorMessages.USERNAME_CANNOT_BE_EMPTY)
    private String username;
    @NotBlank(message = ErrorMessages.PASSWORD_CANNOT_BE_EMPTY)
    private String password;
    @NotBlank(message = ErrorMessages.PASSWORD_CONFIRMATION_CANNOT_BE_EMPTY)
    private String confirmPassword;


}
