package com.generic.service;

import com.generic.model.User;
import com.generic.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;


import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {
    final UserRepository userRepository;
    public static final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean authenticateUser(String email, String password) {
        User user = userRepository.getUserByEmail(email);
        if(user.getPassword().equals(password)) {
            return true;
        }
        return false;
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public User getUserById(String userId) {
        return userRepository.getUserById(userId);
    }

    public int addUser(String email, String password) throws DataIntegrityViolationException {
        return userRepository.addUser(email, password);
    }

    public String generateJwtToken(String email) {
        long expirationMillis = System.currentTimeMillis() + 3600 * 10 * 1000; // 10 hours
        Date expirationDate = new Date(expirationMillis);
        String jwtToken = Jwts.builder()
                .setSubject(email)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();

        return jwtToken;
    }

    public ResponseEntity<String> validateToken(String tokenHeader) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            Date expirationDate = claimsJws.getBody().getExpiration();
            if (expirationDate.before(new Date())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Token is expired");
            }
            return ResponseEntity.ok("Token is valid");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid token");
        }
    }
}
