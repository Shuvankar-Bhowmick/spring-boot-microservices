package com.app.ecom.dto.request;

import com.app.ecom.dto.AddressDto;
import com.app.ecom.model.UserRole;
import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private UserRole role;
    private AddressDto address;
}

