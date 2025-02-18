package com.eric.ecommerce_user_service.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data // Lombok generates getters, setters, equals, hashCode, toString
//@NoArgsConstructor // Generates a no-arg constructor
//@AllArgsConstructor // Generates an all-arg constructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Getter
    @Setter
    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications; // One user can have many notifications

    public User() {}

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

}
