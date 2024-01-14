package com.example.ecommerce.dtos;

import com.example.ecommerce.model.Address;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthRequestDto {
    private String name;
    private String email;
    private String password;
    private Address address;
}
