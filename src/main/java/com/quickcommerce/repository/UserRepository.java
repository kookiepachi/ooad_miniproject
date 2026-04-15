package com.quickcommerce.repository;

import com.quickcommerce.entity.User;
import com.quickcommerce.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    List<User> findByRole(UserRole role);
    List<User> findByIsActive(Boolean isActive);
    Boolean existsByEmail(String email);
}
