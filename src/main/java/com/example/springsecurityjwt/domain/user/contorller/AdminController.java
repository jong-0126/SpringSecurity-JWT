package com.example.springsecurityjwt.domain.user.contorller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public String adminOnly(){
        return "관리자만 접근 가능한 API 입니다.";
    }
}
