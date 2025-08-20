package com.example.springsecurityjwt.domain.user.repository;

import com.example.springsecurityjwt.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByUsername(String username);
}
