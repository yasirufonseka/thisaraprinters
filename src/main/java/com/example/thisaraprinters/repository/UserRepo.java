package com.example.thisaraprinters.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.thisaraprinters.model.UserModel;

public interface UserRepo extends JpaRepository<UserModel, Integer> {
    // find by email
    UserModel findByUsername(String username);

}
