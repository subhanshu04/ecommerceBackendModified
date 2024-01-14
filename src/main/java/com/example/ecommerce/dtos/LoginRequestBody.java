package com.example.ecommerce.dtos;

import lombok.Data;

@Data
public class LoginRequestBody {
    private String email;
    private String password;
}
