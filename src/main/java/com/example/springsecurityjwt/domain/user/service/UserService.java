package com.example.springsecurityjwt.domain.user.service;

import com.example.springsecurityjwt.domain.jwt.JwtUtil;
import com.example.springsecurityjwt.domain.user.dto.Token;
import com.example.springsecurityjwt.domain.user.dto.UserRequestDto;
import com.example.springsecurityjwt.domain.user.dto.UserResponseDto;
import com.example.springsecurityjwt.domain.user.entity.UserEntity;
import com.example.springsecurityjwt.domain.user.entity.UserRole;
import com.example.springsecurityjwt.domain.user.repository.UserRepository;
import jdk.jshell.Snippet;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserResponseDto register(UserRequestDto requestDto) {
        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        String encryptedPassword = passwordEncoder.encode(requestDto.getPassword());

        UserEntity userEntity = new UserEntity(
                requestDto.getUsername(),
                encryptedPassword,
                UserRole.forValue(requestDto.getUserRole())
        );
        userRepository.save(userEntity);

        return new UserResponseDto(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getUserRole()
        );
    }

    public Token login(String username, String password) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if(!passwordEncoder.matches(password, userEntity.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        String token = jwtUtil.createToken(username, userEntity.getUserRole());

        return new Token(token);
    }
}
