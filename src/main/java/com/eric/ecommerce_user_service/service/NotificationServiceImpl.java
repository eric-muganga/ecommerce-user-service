package com.eric.ecommerce_user_service.service;

import com.eric.ecommerce_user_service.Entities.Notification;
import com.eric.ecommerce_user_service.Entities.User;
import com.eric.ecommerce_user_service.repos.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class NotificationServiceImpl implements INotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    @Transactional
    public Notification sendNotification(User user, String message) {
        log.info("Sending notification to user {}: {}", user.getUsername(), message);
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getUserNotifications(User user) {
        return notificationRepository.findByUser(user);
    }
}
