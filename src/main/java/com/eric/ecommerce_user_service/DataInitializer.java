package com.eric.ecommerce_user_service;

import com.eric.ecommerce_user_service.Entities.Role;
import com.eric.ecommerce_user_service.Entities.RoleName;
import com.eric.ecommerce_user_service.repos.RoleRepository;
import com.eric.ecommerce_user_service.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {
    private final RoleRepository roleRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(RoleName.ROLE_USER));
            roleRepository.save(new Role(RoleName.ROLE_ADMIN));
            roleRepository.save(new Role(RoleName.ROLE_MANAGER));
        }
    }
}
