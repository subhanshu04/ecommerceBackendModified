package com.example.ecommerce.services;

import com.example.ecommerce.exceptions.CouldNotSentEmailException;
import com.example.ecommerce.model.EmailVerificationDetailer;
import com.example.ecommerce.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Value("${email.from.address}")
    private String emailFromAddress;
    @Value("${app.frontend.url}")
    private String frontendUrl;
    private JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    //Create Simple mail message, to avoid duplication of code.
    public SimpleMailMessage createMailMessage(){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(emailFromAddress);
        return simpleMailMessage;
    }

    public void sendVerificationEmail(EmailVerificationDetailer verificationEmailDetails) throws CouldNotSentEmailException {
        SimpleMailMessage message = createMailMessage();
        message.setTo(verificationEmailDetails.getUser().getEmailId());
        message.setSubject("Verification Mail for activation of account.");
        message.setText("Please follow the given url to activate your account.\n"+frontendUrl+"/auth/verify?token="+verificationEmailDetails.getToken());
        try{
            javaMailSender.send(message);
        }
        catch(RuntimeException e){
            throw new CouldNotSentEmailException("Email could not be sent due to technical issue. Try again.");
        }
    }

    public void sendPasswordResetLink(User user,String token) throws CouldNotSentEmailException {
        SimpleMailMessage mailBody = createMailMessage();
        mailBody.setTo(user.getEmailId());
        mailBody.setSubject("Password Reset Link");
        mailBody.setText("link to change the password : http://localhost:8080/auth/resetPassword?toke="+token);
        try{
            javaMailSender.send(mailBody);
        }
        catch(Exception e){
            throw new CouldNotSentEmailException("Email could not be sent due to technical issue. Try again.");
        }
    }
}
