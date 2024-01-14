package com.example.ecommerce.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.ecommerce.model.User;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {
    @Value("${jwt.algorithmKey}")
    private String algorithmKey;
    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.expiryTime}")
    private int expiryTime;
    private Algorithm algorithm;
    private static final String username = "adminUser";
    private static final String emailClaim = "email";
    private static final String passwordResetTokenClaim = "Reset_Password";


    //PostConstruct triggers the method as soon as bean is injected.
    @PostConstruct
    public void postConstruct(){
        //Create algorithm to generate the Token
        algorithm = Algorithm.HMAC256(algorithmKey);
    }

    //Generate JWT token for given user and return token as string
    public String generateJWT(User user){
        return JWT.create()
                .withClaim(username,user.getName())  //withClaim is a part of payLoad of JWT, it provides info of the user for which the token is generated
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000*expiryTime))
                .withIssuer(issuer)
                .sign(algorithm);
    }
    public String generateEmailVerificationToken(User user){
        return JWT.create()
                .withClaim(emailClaim,user.getEmailId())  //withClaim is a part of payLoad of JWT, it provides info of the user for which the token is generated
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000*expiryTime))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public String generatePasswordResetVerificationToken(User user){
        return JWT.create()
                .withClaim(passwordResetTokenClaim,user.getEmailId())  //withClaim is a part of payLoad of JWT, it provides info of the user for which the token is generated
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000*60*60))
                .withIssuer(issuer)
                .sign(algorithm);
    }
    public String getEmailForPasswordReset(String token){
        //It will verify whether the Token is created with the given algo or not.
        DecodedJWT jwtDecoded = JWT.require(algorithm).withIssuer(issuer).build().verify(token);

        //return JWT.decode(token).getClaim(username).asString();
        //Instead of JWT.decode we will use jwtDecoded.
        return jwtDecoded.getClaim(passwordResetTokenClaim).asString();
    }
    //Get the username of the user using claim value.
    public String getUsername(String token){
        //It will verify whether the Token is created with the given algo or not.
        DecodedJWT jwtDecoded = JWT.require(algorithm).withIssuer(issuer).build().verify(token);

        //return JWT.decode(token).getClaim(username).asString();
        //Instead of JWT.decode we will use jwtDecoded.
        return jwtDecoded.getClaim(username).asString();
    }
}
