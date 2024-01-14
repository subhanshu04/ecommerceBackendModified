package com.example.ecommerce.exceptions;

import org.springframework.web.bind.annotation.RestControllerAdvice;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(String msg){
        super(msg);
    }
}
