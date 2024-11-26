package com.example.orders.controller;

import com.example.orders.security.dto.JwtResponseDTO;
import com.example.orders.security.dto.LoginDTO;
import com.example.orders.security.dto.RegistrationDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

// THIS IS PRACTICALLY TO DE-CLUTTER THE CONTROLLER, OTHERWISE IT WOULDN'T BE READABLE
// WITH ALL THE DOCUMENTATION ANNOTATIONS
@Tag(name = "Auth", description = "The security API")
public interface AuthApi {

    @Operation(
            summary = "Login to the application",
            description = "Will Login to the application based on the credentials provided"
    )
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "successful login")
    )
    ResponseEntity<JwtResponseDTO> login(@RequestBody LoginDTO dto);


    @Operation(
            summary = "Register to the application",
            description = "Will register to the application"
    )
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "successful login")
    )
    ResponseEntity<Long> register(@RequestBody RegistrationDTO dto);

}
