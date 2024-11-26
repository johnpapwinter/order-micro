package com.example.orders.controller;

import com.example.orders.security.AppPrincipal;
import com.example.orders.security.JwtUtils;
import com.example.orders.security.dto.JwtResponseDTO;
import com.example.orders.security.dto.LoginDTO;
import com.example.orders.security.dto.RegistrationDTO;
import com.example.orders.service.AppUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController implements AuthApi {

    private final AppUserService appUserService;

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthController(AppUserService appUserService,
                          JwtUtils jwtUtils,
                          AuthenticationManager authenticationManager) {
        this.appUserService = appUserService;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<Long> register(@RequestBody RegistrationDTO dto) {
        Long response = appUserService.registerAppUser(dto);

        return ResponseEntity.ok().body(response);
    }


    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateToken(authentication);
        AppPrincipal principal = (AppPrincipal) authentication.getPrincipal();

        JwtResponseDTO responseDTO = JwtResponseDTO.builder()
                .token(jwt)
                .username(principal.getUsername())
                .roles(principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();

        return ResponseEntity.ok().body(responseDTO);
    }

}
