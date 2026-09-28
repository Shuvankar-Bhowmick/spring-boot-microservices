package com.app.ecom.service;

import com.app.ecom.dto.request.UserRequestDto;
import com.app.ecom.dto.request.UserResponseDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserResponseDto> fetchAllUsers();

    String addUser(UserRequestDto user);

    Optional<UserResponseDto> getUserById(Long id);

    Optional<UserResponseDto> updateUserById(Long id, UserRequestDto user);
}
