package com.generic.repository;

import com.generic.model.User;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UserRepository extends PagingAndSortingRepository<User, String> {
    @Query("select * from auth.users")
    public List<User> getAllUsers();

    @Query("select * from auth.users where userId = :userId")
    public User getUserById(String userId);

    @Modifying
    @Query("INSERT INTO auth.users (userId, email, password) VALUES (:userId, :email, :password)")
    public int addUser(String userId, String email, String password);
}
