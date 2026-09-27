package com.app.ecom.service;

import com.app.ecom.dto.UserRequest;
import com.app.ecom.dto.UserResponse;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserResponse> fetchAllUsers();

    String addUser(UserRequest user);

    Optional<UserResponse> getUserById(Long id);

    Optional<UserResponse> updateUserById(Long id, UserRequest user);
}
