package com.example.springsecurityjwt.domain.user.dto;

import com.example.springsecurityjwt.domain.user.entity.UserRole;
import lombok.Getter;

@Getter
public class UserRequestDto {

    private final String username;
    private final String password;
    private final String userRole;

    public UserRequestDto(String username, String password, String userRole) {
        this.username = username;
        this.password = password;
        this.userRole = userRole;
    }
}
