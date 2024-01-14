package com.example.ecommerce.exceptions;

public class WrongPasswordException extends Exception{
    public WrongPasswordException(String msg){
        super(msg);
    }

}
