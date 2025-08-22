package com.example.springsecurityjwt.domain.user.contorller;

import com.example.springsecurityjwt.domain.user.dto.LoginRequestDto;
import com.example.springsecurityjwt.domain.user.dto.Token;
import com.example.springsecurityjwt.domain.user.dto.UserRequestDto;
import com.example.springsecurityjwt.domain.user.dto.UserResponseDto;
import com.example.springsecurityjwt.domain.user.entity.UserEntity;
import com.example.springsecurityjwt.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRequestDto requestDto) {
        return ResponseEntity.ok(userService.register(requestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<Token> login(@RequestBody LoginRequestDto requestDto) {

        return ResponseEntity.ok(userService.login(requestDto.getUsername(), requestDto.getPassword()));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyInfo(@AuthenticationPrincipal UserEntity user){
        return ResponseEntity.ok(new UserResponseDto(user.getId(), user.getUsername(), user.getUserRole()));
    }
}
