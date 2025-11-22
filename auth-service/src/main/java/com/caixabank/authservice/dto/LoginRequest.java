package com.caixabank.authservice.dto;

public record LoginRequest(
        String email,
        String password
) {
}
