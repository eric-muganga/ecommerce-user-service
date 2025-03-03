package com.eric.ecommerce_user_service.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING) // Ensure it's stored as a string in DB
    @Column(nullable = false, unique = true)
    private RoleName name; // Enum field for role names (e.g., ROLE_USER, ROLE_ADMIN)

    public Role(RoleName name) {
        this.name = name;
    }

}
