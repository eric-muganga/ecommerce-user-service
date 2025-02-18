package com.eric.ecommerce_user_service.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor // Generates a no-arg constructor
@AllArgsConstructor // Generates an all-arg constructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String message;

    @CreationTimestamp // Automatically sets timestamp when the row is inserted
    @Column(updatable = false)
    private Timestamp sendDate;


}
