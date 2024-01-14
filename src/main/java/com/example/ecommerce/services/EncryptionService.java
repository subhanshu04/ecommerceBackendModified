package com.example.ecommerce.services;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

    //SaltRound denotes the cost factor, how much hashing should be done to generate salt.
    @Value("${encryption.value.saltRound}")
    private int saltRound;
    private String salt;

    @PostConstruct
    public void genEncryptSalt(){
        //A Salt is a random string of data that is added to a password before it is hashed.
        salt = BCrypt.gensalt(saltRound);
    }

    public String encryptPassword(String pass){
        //Encrypts password
        return BCrypt.hashpw(pass,salt);
    }

    public boolean checkEncryptedPass(String pass,String hashPass){
        //checkpw checks the hash password stored in db with the pass entered by user during login.
        return BCrypt.checkpw(pass,hashPass);
    }
}
