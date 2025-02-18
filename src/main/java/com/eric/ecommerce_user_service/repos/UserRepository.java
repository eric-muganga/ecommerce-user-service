package com.eric.ecommerce_user_service.repos;

import com.eric.ecommerce_user_service.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // find user by username
    Optional<User> findByUsername(String username);

    // find user by email
    Optional<User> findByEmail(String email);

    // Check if a username exists
    boolean existsByUsername(String username);

    // Check if an email exists
    boolean existsByEmail(String email);
}
