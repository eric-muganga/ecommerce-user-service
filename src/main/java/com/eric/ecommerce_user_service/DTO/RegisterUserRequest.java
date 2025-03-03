package com.eric.ecommerce_user_service.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RegisterUserRequest {
    @NotBlank
    @Size(min = 3, max = 50)
    @Schema(description = "Username of the user", example = "johndoe")
    private String username;

    @NotBlank
    @Size(min = 6, max = 100)
    @Schema(description = "Password of the user", example = "password123")
    private String password;

    @NotBlank
    @Email
    @Schema(description = "Email of the user", example = "johndoe@example.com")
    private String email;

    @NotEmpty
    @Schema(description = "Role of the user (USER or ADMIN)", example = "[\"ROLE_USER\"]")
    private Set<String> roles;
}
