package com.example.finance.controller;

import com.example.finance.model.User;

import com.example.finance.service.UserService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")

public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public List<User> getUsers() {
    return userService.getAllUsers();
  }

}
