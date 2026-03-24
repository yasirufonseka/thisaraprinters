package com.example.thisaraprinters.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.thisaraprinters.dto.UserDto;
import com.example.thisaraprinters.model.UserModel;
import com.example.thisaraprinters.repository.UserRepo;



@Service
public class UserService{

@Autowired
private final UserRepo userRepo;

private BCryptPasswordEncoder passwordEncoder;

public UserService(UserRepo userRepo){
    this.userRepo = userRepo;
    
}

public List<UserModel> getAllUsers(){

    return userRepo.findAll();
}

public String saveUser(UserDto user){
    UserModel newUser =  new UserModel();
    newUser.setUsername(user.getUsername());
    newUser.setPassword(passwordEncoder.encode(user.getPassword()));
    newUser.setNote(user.getNote());
    newUser.setRole(user.getRole());
    newUser.setStatus(user.getStatus());
    newUser.setAddeddate(LocalDate.now());
    newUser.setUserphoto(user.getUserphoto());
    userRepo.save(newUser);
    return "User saved successfully";
}
}