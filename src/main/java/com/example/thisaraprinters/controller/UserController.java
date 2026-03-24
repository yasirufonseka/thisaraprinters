package com.example.thisaraprinters.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.thisaraprinters.dto.UserDto;
import com.example.thisaraprinters.model.UserModel;
import com.example.thisaraprinters.service.UserService;

import java.util.List;

import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/user")
public class UserController {


    //show model
    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/usermodel")
    public ModelAndView showUserModel(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("user");
        mav.addObject("users", userService.getAllUsers());
        return mav;
    }

    //get all users
    @GetMapping("/getallusers")
    public ResponseEntity<List<UserModel>> getAllUsers(){
        List<UserModel> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/adduser")
    public String addUser(@RequestBody UserDto user){
        return userService.saveUser(user);
    }   

}
