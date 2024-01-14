package com.example.ecommerce.exceptions;

public class WrongProductNameException extends Exception{
    public WrongProductNameException(String msg){
        super(msg);
    }
}
