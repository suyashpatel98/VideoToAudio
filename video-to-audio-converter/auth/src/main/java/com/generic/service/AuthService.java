package com.generic.service;

import com.generic.model.User;
import com.generic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;


import java.util.List;
import java.util.UUID;

@Service
public class AuthService {
    final UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
