package com.example.springsecurityjwt.domain.user.dto;

import lombok.Getter;

@Getter
public class RefreshRequest {
    private final String refreshToken;

    public RefreshRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
