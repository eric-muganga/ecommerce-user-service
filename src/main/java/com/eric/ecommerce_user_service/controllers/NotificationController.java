package com.eric.ecommerce_user_service.controllers;

import com.eric.ecommerce_user_service.Entities.Notification;
import com.eric.ecommerce_user_service.Entities.User;
import com.eric.ecommerce_user_service.exceptions.ResourceNotFoundException;
import com.eric.ecommerce_user_service.repos.NotificationRepository;
import com.eric.ecommerce_user_service.service.INotificationService;
import com.eric.ecommerce_user_service.service.IUserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
@Slf4j
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
    @GetMapping("/{username}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable("username") String username){
        User user = userService.findUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        List<Notification> notifications = notificationService.getUserNotifications(user);
        return ResponseEntity.ok(notifications);
    }
}
