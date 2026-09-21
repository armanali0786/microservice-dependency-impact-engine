package com.dependencyimpact.serviceregistry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dependencyimpact.serviceregistry.entity.User;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    java.util.Optional<User> findByEmail(String email);
}
