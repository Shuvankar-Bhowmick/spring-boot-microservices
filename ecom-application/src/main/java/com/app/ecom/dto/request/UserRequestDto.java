package com.app.ecom.dto.request;

import com.app.ecom.dto.AddressDto;
import lombok.Data;

@Data
public class UserRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private AddressDto address;
}
