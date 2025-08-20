package com.example.springsecurityjwt.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum UserRole {
    USER, ADMIN;

    @JsonCreator
    public static UserRole forValue(String value) {
        return Arrays.stream(UserRole.values())
                .filter(v -> v.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("역할 값이 잘못되었습니다."));
    }
}
