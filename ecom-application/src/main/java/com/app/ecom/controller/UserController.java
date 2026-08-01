package com.app.ecom.controller;

import com.app.ecom.exception.ResourceNotFoundException;
import com.app.ecom.model.User;
import com.app.ecom.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.fetchAllUsers());
    }

    @GetMapping("/api/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(
                userService.getUserById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id)));
    }

    @PostMapping("/api/users")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(user));
    }

    @PutMapping("/api/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(
                userService.updateUserById(id, user)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + user.getId())));
    }
}
