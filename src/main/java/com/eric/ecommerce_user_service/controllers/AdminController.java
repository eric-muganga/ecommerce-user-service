package com.eric.ecommerce_user_service.controllers;

import com.eric.ecommerce_user_service.DTO.UpdateUserRolesRequest;
import com.eric.ecommerce_user_service.Entities.User;
import com.eric.ecommerce_user_service.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final IUserService userService;

    public AdminController(@Qualifier("userServiceImpl") IUserService userService){
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") // ✅ Fix: Apply at method level & use correct role check
    public String getAdminDashboard() {
        return "Welcome to the admin dashboard!";
    }

    @PatchMapping("/user/{username}/roles")
    public ResponseEntity<User> updateUserRoles(@PathVariable String username,
                                                @RequestBody UpdateUserRolesRequest request) {
        User updatedUser = userService.updateUserRoles(username, request.getRoles());
        return ResponseEntity.ok(updatedUser);
    }

}
