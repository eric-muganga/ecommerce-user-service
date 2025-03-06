package com.eric.ecommerce_user_service.service;

import com.eric.ecommerce_user_service.DTO.UserProfileResponse;
import com.eric.ecommerce_user_service.Entities.Role;
import com.eric.ecommerce_user_service.Entities.RoleName;
import com.eric.ecommerce_user_service.Entities.User;
import com.eric.ecommerce_user_service.repos.RoleRepository;
import com.eric.ecommerce_user_service.repos.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public User registerUser(User user) {
        log.info("Registering new user: {}", user.getUsername());
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsUserByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsUserByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User updateUserRoles(String username, Set<String> roleNames) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Fetch and validate roles
        Set<Role> roles = roleNames.stream()
                .map(roleName -> roleRepository.findByName(RoleName.valueOf(roleName.toUpperCase()))
                        .orElseThrow(() -> new IllegalArgumentException("Invalid role: " + roleName)))
                .collect(Collectors.toSet());

        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Override
    public UserProfileResponse getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserProfileResponse(user.getUsername(), user.getEmail(),
                user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
    }
}
