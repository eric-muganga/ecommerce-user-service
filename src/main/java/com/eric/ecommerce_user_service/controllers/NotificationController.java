package com.eric.ecommerce_user_service.controllers;

import com.eric.ecommerce_user_service.Entities.Notification;
import com.eric.ecommerce_user_service.Entities.User;
import com.eric.ecommerce_user_service.exceptions.ResourceNotFoundException;
import com.eric.ecommerce_user_service.service.INotificationService;
import com.eric.ecommerce_user_service.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/notifications")
@Slf4j
@Tag(name = "Notification Controller", description = "APIs for managing notifications")
public class NotificationController {
    private final INotificationService notificationService;
    private final IUserService userService;

    public NotificationController(@Qualifier("notificationServiceImpl") INotificationService notificationService,
                                  @Qualifier("userServiceImpl") IUserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    /**
     * Send Notification to a User
     */
    @Operation(summary = "Send notification", description = "Send a notification to a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification sent successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/send/{username}")
    public ResponseEntity<Notification> sendNotification(@PathVariable("username") String username,
                                                         @Valid @RequestBody String message){
        User user = userService.findUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for sending notification: " + username));

        Notification notification = notificationService.sendNotification(user, message);
        log.info("Notification sent to user {}: {}", username, notification);
        return ResponseEntity.ok(notification);
    }

    /**
     * Get Notifications for a User
     */
    @Operation(summary = "Get notifications for a user", description = "Retrieve all notifications for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{username}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable("username") String username){
        User user = userService.findUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        List<Notification> notifications = notificationService.getUserNotifications(user);
        return ResponseEntity.ok(notifications);
    }
}
