package com.app.ecom.controller;

import com.app.ecom.dto.request.UserRequestDto;
import com.app.ecom.dto.request.UserResponseDto;
import com.app.ecom.exception.ResourceNotFoundException;
import com.app.ecom.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.fetchAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id)));
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserRequestDto user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id,
                                                      @RequestBody UserRequestDto user) {
        return ResponseEntity.ok(userService.updateUserById(id, user).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id)));
    }

}
