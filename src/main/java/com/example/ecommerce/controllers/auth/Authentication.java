package com.example.ecommerce.controllers.auth;

import com.example.ecommerce.dtos.AuthRequestDto;
import com.example.ecommerce.dtos.LoginRequestBody;
import com.example.ecommerce.dtos.LoginResponseBody;
import com.example.ecommerce.exceptions.CouldNotSentEmailException;
import com.example.ecommerce.exceptions.UserNotFoundException;
import com.example.ecommerce.exceptions.UserNotVerifiedException;
import com.example.ecommerce.exceptions.WrongPasswordException;
import com.example.ecommerce.model.User;
import com.example.ecommerce.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class Authentication {

    private UserService userService;

    public Authentication(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody AuthRequestDto authRequestDto) {
        try {
            userService.register(authRequestDto);
            return new ResponseEntity<>("User successfully registered", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestBody loginRequestBody) {
        try {
            LoginResponseBody loginResponseBody = userService.loginUser(loginRequestBody);
            loginResponseBody.setVerificationSuccess(true);
            return new ResponseEntity<>(loginResponseBody, HttpStatus.OK);
        }
        // If user is not verified then we need to catch the exception and print the
        // Reason for failure.
        catch (UserNotVerifiedException e) {
            LoginResponseBody loginResponseBody = new LoginResponseBody();
            loginResponseBody.setVerificationSuccess(false);
            String reason = "USER_NOT_VERIFIED.";
            if (e.isNewEmailSent()) {
                reason += "_EMAIL_SENT.";
            }
            loginResponseBody.setFailureReason(reason);
            return new ResponseEntity<>(loginResponseBody, HttpStatus.FORBIDDEN);
        } catch (CouldNotSentEmailException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // This method basically authenticates the user on the basis of token created.
    // Spring on getting authentication cast the authentication to User and provide
    // the user with authentication.
    @GetMapping("/me")
    public User getLoggedInUserProfile(@AuthenticationPrincipal User user) {
        // @AuthenticationPrincipal will get you the user which is associated to the
        // Token provided in header. This uses getUsername method in JWTService Class.
        return user;
    }

    @PostMapping("/verify")
    public ResponseEntity verifyUserEmail(@RequestParam String token) throws CouldNotSentEmailException {
        if (userService.verifyToken(token)) {
            return ResponseEntity.ok().build();
        } else {
            // Conflict states that either user is already verified or token doesn't exist.
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/requestLink")
    public ResponseEntity requestPasswordResetLink(@RequestBody LoginRequestBody loginRequestBody)
            throws CouldNotSentEmailException, UserNotFoundException, WrongPasswordException {
        String message = userService.requestPasswordResetLink(loginRequestBody);
        return ResponseEntity.ok().body(message);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity resetPassword(@RequestParam String token, @RequestParam String password)
            throws UserNotFoundException {
        String msg = userService.resetPassword(token, password);
        return ResponseEntity.ok().body(msg);
    }
}
