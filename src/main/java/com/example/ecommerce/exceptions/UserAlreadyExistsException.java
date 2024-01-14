package com.example.ecommerce.exceptions;

import org.springframework.web.bind.annotation.RestControllerAdvice;


public class UserAlreadyExistsException extends Exception{
    public UserAlreadyExistsException(String msg){
        super(msg);
    }
}
