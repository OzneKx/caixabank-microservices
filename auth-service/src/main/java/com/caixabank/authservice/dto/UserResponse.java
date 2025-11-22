package com.caixabank.authservice.dto;

public record UserResponse(
        Long id,
        String name,
        String email,
        String role
) {
}
