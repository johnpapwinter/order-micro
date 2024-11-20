package com.example.orders.controller;

import com.example.orders.security.dto.JwtResponseDTO;
import com.example.orders.security.dto.LoginDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

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

}
