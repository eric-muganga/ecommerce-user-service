package com.eric.ecommerce_user_service.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class UpdateUserRolesRequest {
    @NotEmpty
    @Schema(description = "List of roles to assign to the user", example = "[\"ROLE_USER\", \"ROLE_MANAGER\"]")
    private Set<String> roles; // Accepts role names like "ROLE_USER", "ROLE_ADMIN"
}
