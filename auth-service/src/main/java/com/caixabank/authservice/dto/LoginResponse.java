package com.caixabank.authservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Contains JWT token and user details issued upon successfully authentication.")
public record LoginResponse(
        @Schema(
                description = "JWT token generated after successful login.",
                example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJudXdlQG51d2UuY29tIiwiZXhwIjoxNjk1OTgxMDUxfQ.xxxxxxxxxxxxxx"
        )
        String token,

        @Schema(
                description = "Type of the token.",
                example = "Bearer"
        )
        String type,

        @Schema(
                description = "Token expiration time in seconds.",
                example = "3600"
        )
        Long expiresIn,

        @Schema(
                description = "User details."
        )
        UserResponse user
) {
}
