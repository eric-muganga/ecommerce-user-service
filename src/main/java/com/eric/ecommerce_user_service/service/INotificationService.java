package com.eric.ecommerce_user_service.service;

import com.eric.ecommerce_user_service.Entities.Notification;
import com.eric.ecommerce_user_service.Entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface INotificationService {
    Notification sendNotification(User user, String message); // send a notification

    List<Notification> getUserNotifications(User user); // Retrieve notifications for a user
}
