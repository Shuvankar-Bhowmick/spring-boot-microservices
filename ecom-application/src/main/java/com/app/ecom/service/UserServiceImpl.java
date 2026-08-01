package com.app.ecom.service;

import com.app.ecom.model.User;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserServiceImpl implements UserService {
    private static AtomicInteger id = new AtomicInteger(0);
    private List<User> users = new ArrayList<>();

    public List<User> fetchAllUsers() {
        return users;
    }

    public String addUser(User user) {
        user.setId((long) id.incrementAndGet());
        users.add(user);
        return "User added successfully";
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null));
    }

    @Override
    public Optional<User> updateUserById(Long id, User user) {
        if (id == null || user == null) {
            return Optional.empty();
        }

        // Ignore id in request body and use the path id; synchronize to avoid concurrent modifications
        synchronized (users) {
            Optional<User> existingUser = getUserById(id);
            if (existingUser.isPresent()) {
                User updatedUser = getUpdatedUser(user, existingUser);

                return Optional.of(updatedUser);
            }
            return Optional.empty();
        }
    }

    private static @NonNull User getUpdatedUser(User user, Optional<User> existingUser) {
        User updatedUser = existingUser.get();

        // Merge only non-null/non-blank fields from the incoming payload
        if (user.getFirstName() != null && !user.getFirstName().isBlank()) {
            updatedUser.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null && !user.getLastName().isBlank()) {
            updatedUser.setLastName(user.getLastName());
        }
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            updatedUser.setEmail(user.getEmail());
        }
        return updatedUser;
    }
}
