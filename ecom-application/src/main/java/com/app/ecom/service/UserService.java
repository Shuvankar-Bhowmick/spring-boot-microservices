package com.app.ecom.service;

import com.app.ecom.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> fetchAllUsers();

    String addUser(User user);

    Optional<User> getUserById(Long id);

    Optional<User> updateUserById(Long id, User user);
}
