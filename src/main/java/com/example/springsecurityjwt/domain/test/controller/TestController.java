package com.example.springsecurityjwt.domain.test.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @PostMapping("/hello")
    public ResponseEntity<String> rateLimitTest() {
        return ResponseEntity.ok("Hello, world!!");
    }
}
