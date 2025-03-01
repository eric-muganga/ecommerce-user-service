package com.eric.ecommerce_user_service.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank
    @Schema(description = "Username of the user", example = "johndoe")
    private String username;

    @NotBlank
    @Schema(description = "Password of the user", example = "password123")
    private String password;
}
