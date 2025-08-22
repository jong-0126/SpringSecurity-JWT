package com.example.springsecurityjwt.domain.user.dto;

import lombok.Getter;

@Getter
public class Token {
    private final String accessToken;

    public Token(String accessToken) {
        this.accessToken = accessToken;
    }
}
