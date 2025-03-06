package com.eric.ecommerce_user_service.service;

import com.eric.ecommerce_user_service.DTO.UserProfileResponse;
import com.eric.ecommerce_user_service.Entities.User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public interface IUserService {
    User registerUser(User user); // register a  new user

    Optional<User> findUserByUsername(String username); // Find user by username

    Optional<User> findUserByEmail(String email); // Find user by email

    boolean existsUserByUsername(String username); // Check if username exists

    boolean existsUserByEmail(String email); // check if the email exists

    User updateUserRoles(String username, Set<String> roleNames); //

    UserProfileResponse getUserProfile(String username);

}
