package com.app.ecom.service;

import com.app.ecom.dto.AddressDto;
import com.app.ecom.dto.UserRequest;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.model.Address;
import com.app.ecom.model.User;
import com.app.ecom.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static @NonNull User getUpdatedUser(User incomingUser, User existingUser) {
        // Merge only non-null/non-blank fields from the incoming payload
        if (incomingUser.getFirstName() != null && !incomingUser.getFirstName().isBlank()) {
            existingUser.setFirstName(incomingUser.getFirstName());
        }
        if (incomingUser.getLastName() != null && !incomingUser.getLastName().isBlank()) {
            existingUser.setLastName(incomingUser.getLastName());
        }
        if (incomingUser.getEmail() != null && !incomingUser.getEmail().isBlank()) {
            existingUser.setEmail(incomingUser.getEmail());
        }
        return existingUser;
    }

    public List<UserResponse> fetchAllUsers() {
        return userRepository.findAll().stream().map(this::mapToUserResponse).toList();
    }

    public String addUser(UserRequest user) {
        userRepository.save(this.mapToUserFromUserRequest(user));
        return "User added successfully";
    }

    @Override
    public Optional<UserResponse> getUserById(Long id) {
        return userRepository.findById(id).map(this::mapToUserResponse);
    }

    @Override
    public Optional<UserResponse> updateUserById(Long id, UserRequest user) {
        if (id == null || user == null) {
            return Optional.empty();
        }

        return userRepository.findById(id)
                .map(existingUser -> getUpdatedUser(mapToUserFromUserRequest(user), existingUser))
                .map(userRepository::save)
                .map(this::mapToUserResponse);
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone());
        userResponse.setRole(user.getRole());
        if (user.getAddress() != null) {
            Address address = user.getAddress();
            AddressDto addressResponse = new AddressDto();
            addressResponse.setStreet(address.getStreet());
            addressResponse.setCity(address.getCity());
            addressResponse.setState(address.getState());
            addressResponse.setCountry(address.getCountry());
            addressResponse.setZipcode(address.getZipcode());
            userResponse.setAddress(addressResponse);
        }
        return userResponse;
    }

    private User mapToUserFromUserRequest(UserRequest userRequest) {
        User user = new User();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        if (userRequest.getAddress() != null) {
            AddressDto addressDto = userRequest.getAddress();
            Address address = new Address();
            address.setStreet(addressDto.getStreet());
            address.setCity(addressDto.getCity());
            address.setState(addressDto.getState());
            address.setCountry(addressDto.getCountry());
            address.setZipcode(addressDto.getZipcode());
            user.setAddress(address);
        }
        return user;
    }
}
