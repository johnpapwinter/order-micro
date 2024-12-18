package com.example.orders.security.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class JwtResponseDTO {

    private String token;
    private String username;
    private List<String> roles;

}
