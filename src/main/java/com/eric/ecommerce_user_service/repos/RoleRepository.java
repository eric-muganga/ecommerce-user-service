package com.eric.ecommerce_user_service.repos;

import com.eric.ecommerce_user_service.Entities.Role;
import com.eric.ecommerce_user_service.Entities.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
