package com.example.ecommerce.exceptions;

public class CouldNotSentEmailException extends Exception{
    public CouldNotSentEmailException(String msg){
        super(msg);
    }

}
