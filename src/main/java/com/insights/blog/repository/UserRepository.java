package com.insights.blog.repository;

import com.insights.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    // Method to delete all users
    void deleteAll();
}
