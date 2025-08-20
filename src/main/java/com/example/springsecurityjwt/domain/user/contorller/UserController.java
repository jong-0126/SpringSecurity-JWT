package com.example.springsecurityjwt.domain.user.contorller;

import com.example.springsecurityjwt.domain.user.dto.UserRequestDto;
import com.example.springsecurityjwt.domain.user.dto.UserResponseDto;
import com.example.springsecurityjwt.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRequestDto requestDto) throws IllegalAccessException {
        return ResponseEntity.ok(userService.register(requestDto));
    }
}
