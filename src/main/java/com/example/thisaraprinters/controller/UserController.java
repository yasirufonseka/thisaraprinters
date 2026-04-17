package com.example.thisaraprinters.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.thisaraprinters.dto.UserDto;
import com.example.thisaraprinters.model.EmployeeModel;
import com.example.thisaraprinters.model.RoleModel;
import com.example.thisaraprinters.model.UserModel;
import com.example.thisaraprinters.service.UserService;

import java.util.List;
import java.util.Map;

import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/user")
public class UserController {

    // show model
    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/usermodel")
    public ModelAndView showUserModel() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("user");
        mav.addObject("users", userService.getAllUsers());
        return mav;
    }

    // get all users
    @GetMapping("/getallusers")
    public ResponseEntity<List<UserModel>> getAllUsers() {
        List<UserModel> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/add/user")
    public ResponseEntity<Map<String, String>> addUser(@RequestBody UserDto user) {
        return ResponseEntity.status(200).body(Map.of("message", userService.saveUser(user)));
    }

    @PostMapping("/update/user/{id}")
    public ResponseEntity<Map<String, String>> updateUser(@RequestBody UserDto user, @PathVariable("id") Integer id) {
        return ResponseEntity.status(200).body(Map.of("message", userService.updateUser(user, id)));
    }

    @GetMapping("/getemployeelist")
    public ResponseEntity<List<EmployeeModel>> getEmployeeList() {
        return ResponseEntity.status(200).body(userService.getEmployeeList());
    }

    @GetMapping("/getroles")
    public ResponseEntity<List<RoleModel>> getRoles() {
        return ResponseEntity.status(200).body(userService.getAllRoles());
    }

    @GetMapping("/getuserbyid/{id}")
    public ResponseEntity<UserModel> getUserById(@PathVariable("id") int id) {
        return ResponseEntity.status(200).body(userService.getUserById(id));
    }

}
