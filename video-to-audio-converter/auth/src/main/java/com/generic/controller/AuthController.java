package com.generic.controller;

import com.generic.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        int rowsAffected = 0;
        try {
            rowsAffected = authService.addUser(email, password);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Email already exists");
        }

        if (rowsAffected > 0) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("User registered successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to register user");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam(name = "emailId", required = true) String email,
                                        @RequestParam(name = "password", required = true) String password) {
        boolean isAuthenticated = authService.authenticateUser(email, password);

        if (isAuthenticated) {
            String jwtToken = authService.generateJwtToken(email);
            return ResponseEntity.ok(jwtToken);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestHeader(name = "Authorization") String tokenHeader) {
        return authService.validateToken(tokenHeader);
    }
}
