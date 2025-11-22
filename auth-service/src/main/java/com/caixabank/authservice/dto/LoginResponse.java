package com.caixabank.authservice.dto;

public record LoginResponse(
        String token,
        String type,
        Long expiresIn,
        UserResponse user
) {
}
