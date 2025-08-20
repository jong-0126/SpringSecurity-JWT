package com.example.springsecurityjwt.domain.user.dto;

import com.example.springsecurityjwt.domain.user.entity.UserRole;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private final Long id;
    private final String username;
    private final UserRole userRole;

    public UserResponseDto(Long id, String username, UserRole userRole) {
        this.id = id;
        this.username = username;
        this.userRole = userRole;
    }
}
