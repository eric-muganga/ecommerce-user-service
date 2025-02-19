package com.eric.ecommerce_user_service.controllers;

import com.eric.ecommerce_user_service.Entities.User;
import com.eric.ecommerce_user_service.service.IUserService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    private final IUserService userService;

    public UserController(@Qualifier("userServiceImpl") IUserService userService) {
        this.userService = userService;
    }

    /**
     * Register a New User
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        if(userService.existsUserByUsername(user.getUsername()) ||
                userService.existsUserByEmail(user.getEmail())) {
            log.warn("Username or email already exists: {}", user.getUsername());
            return ResponseEntity.badRequest().build();
        }

        User registeredUser = userService.registerUser(user);
        log.info("Registered user successfully: {}", registeredUser.getUsername());
        return ResponseEntity.ok(registeredUser);
    }


    /**
     * Get User by Username
     */
    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.findUserByUsername(username);

        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    /**
     * Check if Username Exists
     */
    @GetMapping("/exists/username/{username}")
    public ResponseEntity<Boolean> existsUserByUsername(@PathVariable String username) {
        boolean exists = userService.existsUserByUsername(username);
        return ResponseEntity.ok(exists);
    }

    /**
     * check if email exists
     */
    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> existsUserByEmail(@PathVariable String email) {
        boolean exists = userService.existsUserByEmail(email);
        return ResponseEntity.ok(exists);
    }

}
