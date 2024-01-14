package com.example.ecommerce.service;

import com.example.ecommerce.dtos.AuthRequestDto;
import com.example.ecommerce.exceptions.UserAlreadyExistsException;
import com.example.ecommerce.services.UserService;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetup;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    /** Extension for mocking email sending. */
    @RegisterExtension
    private static GreenMailExtension greenMailExtension = new GreenMailExtension(ServerSetup.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("springboot","secret"))
            .withPerMethodLifecycle(true);
    @Test
    @Transactional
    public void testRegisterUser() throws MessagingException {
        AuthRequestDto authRequestDto = new AuthRequestDto();
        authRequestDto.setName("userA");    //This name of the user is inserted to h2 database in data.sql file SO it will throw username already exists
        authRequestDto.setEmail("userServiceTestA@gmail.com");
        authRequestDto.setPassword("userServiceTestPassword@123");
        Assertions.assertThrows(UserAlreadyExistsException.class,
                () -> userService.register(authRequestDto),"Username should be in use");
        authRequestDto.setName("userServiceTestA");
        authRequestDto.setEmail("userA@junit.com");  //This emailId is inserted t h2 , so it throws emailId already exists exception.
        Assertions.assertThrows(UserAlreadyExistsException.class,
                () -> userService.register(authRequestDto),"Email id should be in use");
        authRequestDto.setEmail("userServiceTestA@gmail.com");
        Assertions.assertDoesNotThrow(()->userService.register(authRequestDto),"User should register successfully.");
        Assertions.assertEquals(authRequestDto.getEmail(),
                greenMailExtension.getReceivedMessages()[0].getRecipients(Message.RecipientType.TO)[0].toString());
    }
}
