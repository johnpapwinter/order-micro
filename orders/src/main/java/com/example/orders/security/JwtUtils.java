package com.example.orders.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.issuer}")
    private String jwtIssuer;

    @Value("${app.jwt.expiration}")
    private int jwtExpiration;


    public String generateToken(Authentication authentication) {
        AppPrincipal principal = (AppPrincipal) authentication.getPrincipal();
        String username = principal.getUsername();

        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(new Date().getTime() + jwtExpiration))
                .withClaim("username", username)
                .withIssuer(jwtIssuer)
                .sign(Algorithm.HMAC512(jwtSecret));
    }


    public String getUsernameFromToken(String token) {

        return JWT.decode(token).getClaim("username").asString();
    }


    public boolean verifyToken(String token) {

        return decodeToken(token) != null;
    }


    public DecodedJWT decodeToken(String token) {
        DecodedJWT decodedJWT = null;
        try {
            Algorithm algorithm = Algorithm.HMAC512(jwtSecret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(jwtIssuer)
                    .build();
            decodedJWT = verifier.verify(token);
        } catch (JWTVerificationException e) {
            LOGGER.error("Error: {}", e.getMessage());
        }

        return decodedJWT;
    }



}
