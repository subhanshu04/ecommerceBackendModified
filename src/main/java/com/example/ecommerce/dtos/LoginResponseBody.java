package com.example.ecommerce.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseBody {
    private String jwt;
    private boolean isVerificationSuccess;
    private String failureReason;
}
