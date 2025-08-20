package com.example.springsecurityjwt.domain.user.service;

import com.example.springsecurityjwt.domain.user.dto.UserRequestDto;
import com.example.springsecurityjwt.domain.user.dto.UserResponseDto;
import com.example.springsecurityjwt.domain.user.entity.UserEntity;
import com.example.springsecurityjwt.domain.user.entity.UserRole;
import com.example.springsecurityjwt.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto register(UserRequestDto requestDto) throws IllegalAccessException {
        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new IllegalAccessException("이미 존재하는 사용자 입니다.");
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
}
