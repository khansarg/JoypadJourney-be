package com.example.joypadjourney.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.joypadjourney.model.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String>{
    Optional<Customer> findByEmail(String email);
}
