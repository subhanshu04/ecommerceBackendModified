package com.example.ecommerce.exceptions;

public class UserNotVerifiedException extends Exception{
    private boolean newEmailSent;
    public boolean isNewEmailSent() {
        return newEmailSent;
    }
    public UserNotVerifiedException(boolean newEmailSent){
        this.newEmailSent=newEmailSent;
    }
}
