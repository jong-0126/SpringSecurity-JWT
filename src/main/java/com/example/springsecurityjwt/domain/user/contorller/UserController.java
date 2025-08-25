package com.example.springsecurityjwt.domain.user.contorller;

import com.example.springsecurityjwt.domain.jwt.JwtUtil;
import com.example.springsecurityjwt.domain.user.dto.*;
import com.example.springsecurityjwt.domain.user.entity.UserEntity;
import com.example.springsecurityjwt.domain.user.repository.UserRepository;
import com.example.springsecurityjwt.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRequestDto requestDto) {
        return ResponseEntity.ok(userService.register(requestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<Token> login(@RequestBody LoginRequestDto requestDto) {

        return ResponseEntity.ok(userService.login(requestDto.getUsername(), requestDto.getPassword()));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyInfo(@AuthenticationPrincipal UserEntity user) {
        return ResponseEntity.ok(new UserResponseDto(user.getId(), user.getUsername(), user.getUserRole()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Token> refreshToken(@RequestBody RefreshRequest request){
        String refreshToken = request.getRefreshToken();

        if(!jwtUtil.validateToken(refreshToken)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        String username = jwtUtil.getUsername(refreshToken);
        String saved = redisTemplate.opsForValue().get("refresh:" + username);

        if(saved == null || !saved.equals(refreshToken)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        String newAccessToken = jwtUtil.createToken(username, userEntity.getUserRole());

        return ResponseEntity.ok(new Token(newAccessToken, refreshToken));
    }
}
