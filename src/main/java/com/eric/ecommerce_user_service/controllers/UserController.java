package com.eric.ecommerce_user_service.controllers;

import com.eric.ecommerce_user_service.Entities.User;
import com.eric.ecommerce_user_service.exceptions.ResourceNotFoundException;
import com.eric.ecommerce_user_service.exceptions.UnauthorizedException;
import com.eric.ecommerce_user_service.exceptions.UserAlreadyExistsException;
import com.eric.ecommerce_user_service.service.IUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
@Slf4j
@Tag(name = "User Controller", description = "APIs for user management")
public class UserController {

    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(@Qualifier("userServiceImpl") IUserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Register a New User
     */
    @Operation(summary = "Register a new user", description = "Create a new user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Username or email already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        if(userService.existsUserByUsername(user.getUsername()) ||
                userService.existsUserByEmail(user.getEmail())) {
            log.warn("Username or email already exists: {}", user.getUsername());
            throw new UserAlreadyExistsException("Username or email already exists: " + user.getUsername());
        }

        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User registeredUser = userService.registerUser(user);
        log.info("Registered user successfully: {}", registeredUser.getUsername());
        return ResponseEntity.ok(registeredUser);
    }

    /**
     * login a User
     */
    @Operation(summary = "Login user", description = "Authenticate user and return JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody User loginRequest) {
        User user = userService.findUserByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + loginRequest.getUsername()));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) { // Ideally, use a PasswordEncoder
            throw new UnauthorizedException("Invalid username or password");
        }

        String jwtToken = jwtUtil.generateToken(user.getUsername());

        return ResponseEntity.ok("Bearer " + jwtToken);
    }

    /**
     * Get User by Username
     */
    @Operation(summary = "Get user by username", description = "Retrieve user details using username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.findUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        return ResponseEntity.ok(user);
    }


    /**
     * Check if Username Exists
     */
    @Operation(summary = "Check if username exists", description = "Verify if a username is already taken")
    @GetMapping("/exists/username/{username}")
    public ResponseEntity<Boolean> existsUserByUsername(@PathVariable String username) {
        boolean exists = userService.existsUserByUsername(username);
        return ResponseEntity.ok(exists);
    }

    /**
     * check if email exists
     */
    @Operation(summary = "Check if email exists", description = "Verify if an email is already registered")
    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> existsUserByEmail(@PathVariable String email) {
        boolean exists = userService.existsUserByEmail(email);
        return ResponseEntity.ok(exists);
    }

}
