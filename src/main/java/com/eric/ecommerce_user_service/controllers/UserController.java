package com.eric.ecommerce_user_service.controllers;

import com.eric.ecommerce_user_service.DTO.LoginRequest;
import com.eric.ecommerce_user_service.DTO.RegisterUserRequest;
import com.eric.ecommerce_user_service.Entities.User;
import com.eric.ecommerce_user_service.auth.JwtUtil;
import com.eric.ecommerce_user_service.exceptions.ResourceNotFoundException;
import com.eric.ecommerce_user_service.exceptions.UnauthorizedException;
import com.eric.ecommerce_user_service.exceptions.UserAlreadyExistsException;
import com.eric.ecommerce_user_service.service.IUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/user")
@Slf4j
@Tag(name = "User Controller", description = "APIs for user management")
@SecurityRequirement(name = "bearerAuth") // <==== All endpoints require JWT **except where overridden**
public class UserController {

    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserController(@Qualifier("userServiceImpl") IUserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
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
    @SecurityRequirement(name = "")  // this **disables** JWT authentication for this method
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody RegisterUserRequest request) {
        if(userService.existsUserByUsername(request.getUsername()) ||
                userService.existsUserByEmail(request.getEmail())) {
            log.warn("Username or email already exists: {}", request.getUsername());
            throw new UserAlreadyExistsException("Username or email already exists: " + request.getUsername());
        }


        User user = new User();
        user.setUsername(request.getUsername());
        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Encrypt password
        user.setEmail(request.getEmail());

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
    @SecurityRequirement(name = "")  // this **disables** JWT authentication for this method
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        User user = userService.findUserByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + loginRequest.getUsername()));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) { // Ideally, use a PasswordEncoder
            throw new UnauthorizedException("Invalid username or password");
        }

        String jwtToken = jwtUtil.generateToken(user.getUsername());

        return ResponseEntity.ok(Map.of("token", "Bearer " + jwtToken));
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
