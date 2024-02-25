package com.generic.controller;

import com.generic.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam(name = "emailId", required = true) String email,
                                           @RequestParam(name = "password", required = true) String password) {
        int rowsAffected = authService.addUser(email, password);

        if (rowsAffected > 0) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("User registered successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to register user");
        }
    }
}
