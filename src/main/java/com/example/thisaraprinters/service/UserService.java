package com.example.thisaraprinters.service;

import com.example.thisaraprinters.repository.EmployeeRepo;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.thisaraprinters.dto.UserDto;
import com.example.thisaraprinters.model.EmployeeModel;
import com.example.thisaraprinters.model.RoleModel;
import com.example.thisaraprinters.model.UserModel;
import com.example.thisaraprinters.repository.RoleRepo;
import com.example.thisaraprinters.repository.UserRepo;

@Service
@Transactional
public class UserService {

    private final EmployeeRepo employeeRepo;
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    // private BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, RoleRepo roleRepo, EmployeeRepo employeeRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.employeeRepo = employeeRepo;

    }

    public List<UserModel> getAllUsers() {

        return userRepo.findAll();
    }

    public String saveUser(UserDto user) {
        UserModel newUser = new UserModel();
        if (user.getEmployeeid() != null) {
            newUser.setEmployeeid(employeeRepo.findById(user.getEmployeeid()).orElse(null));
        }
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());
        newUser.setNote(user.getNote());
        
        if (user.getRoleIds() != null) {
            List<RoleModel> roles = roleRepo.findAllById(user.getRoleIds());
            newUser.setRoles(roles);
        }

        newUser.setStatus(user.getStatus());
        newUser.setAddeddate(LocalDate.now());
        newUser.setUserphoto(user.getUserphoto());
        userRepo.save(newUser);
        return "User saved successfully";
    }

    // get all employees
    public List<EmployeeModel> getEmployeeList() {
        return employeeRepo.findAll();
    }

    public String updateUser(UserDto user, Integer id) {
        if (id == null) {
            return "User ID is required for update";
        }
        UserModel existingUser = userRepo.findById(id).orElse(null);
        if (existingUser == null) {
            return "User not found";
        }
        
        if (user.getEmployeeid() != null) {
            existingUser.setEmployeeid(employeeRepo.findById(user.getEmployeeid()).orElse(null));
        }
        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            existingUser.setUsername(user.getUsername());
        }
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(user.getPassword());
        }
        if (user.getNote() != null) {
            existingUser.setNote(user.getNote());
        }
        if (user.getStatus() != null) {
            existingUser.setStatus(user.getStatus());
        }
        
        if (user.getRoleIds() != null) {
            List<RoleModel> roles = roleRepo.findAllById(user.getRoleIds());
            existingUser.setRoles(roles);
        }

        existingUser.setUpdatedate(LocalDate.now());
        
        userRepo.save(existingUser);
        return "User updated successfully";
    }

    public UserModel getUserById(int id) {
        return userRepo.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
    };

    public List<RoleModel> getAllRoles() {
        return roleRepo.findAll();
    }
}
