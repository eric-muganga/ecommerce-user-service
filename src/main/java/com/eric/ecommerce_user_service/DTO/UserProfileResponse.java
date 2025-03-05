package com.eric.ecommerce_user_service.DTO;

import com.eric.ecommerce_user_service.Entities.RoleName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class UserProfileResponse {
    private String username;
    private String email;
    private Set<RoleName> roles;
}
