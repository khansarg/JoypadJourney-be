package com.example.joypadjourney.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.joypadjourney.model.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
}