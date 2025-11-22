package com.caixabank.authservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank(message = "Name is required.")
        @Size(max = 100)
        @JsonProperty("name")
        String name,

        @NotBlank(message = "Email is required.")
        @Size(max = 120)
        @JsonProperty("email")
        String email,

        @NotBlank(message = "Password is required.")
        @JsonProperty("password")
        String password
) {
}
