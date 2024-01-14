package com.example.ecommerce.services;

import com.example.ecommerce.dtos.AuthRequestDto;
import com.example.ecommerce.dtos.LoginRequestBody;
import com.example.ecommerce.dtos.LoginResponseBody;
import com.example.ecommerce.exceptions.*;
import com.example.ecommerce.model.EmailVerificationDetailer;
import com.example.ecommerce.dtos.NewPasswordDetails;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repositories.EmailVerificationRepo;
import com.example.ecommerce.repositories.UserServiceRepo;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserServiceRepo userServiceRepo;
    private EncryptionService encryptionService;
    private JWTService jwtService;
    private EmailService emailService;
    private EmailVerificationRepo emailVerificationRepo;

    public UserService(UserServiceRepo userServiceRepo, EncryptionService encryptionService, JWTService jwtService, EmailService emailService, EmailVerificationRepo emailVerificationRepo) {
        this.userServiceRepo = userServiceRepo;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.emailVerificationRepo = emailVerificationRepo;
    }
    public User register(AuthRequestDto authRequestDto) throws UserAlreadyExistsException, CouldNotSentEmailException {
        if(userServiceRepo.findByNameIgnoreCase(authRequestDto.getName()).isPresent() ||
                userServiceRepo.findByEmailIdIgnoreCase(authRequestDto.getEmail()).isPresent()){
            throw new UserAlreadyExistsException("User already present");
        }

        User user = new User();
        user.setName(authRequestDto.getName());
        user.setEmailId(authRequestDto.getEmail());
        user.setPassword(encryptionService.encryptPassword(authRequestDto.getPassword()));

        //Create emailVerificationDetail object and send verification email.After sending save the object to Database.
        EmailVerificationDetailer emailVerificationDetailer = createEmailVerificationDetailer(user);
        emailService.sendVerificationEmail(emailVerificationDetailer);
        emailVerificationRepo.save(emailVerificationDetailer);

        user.getEmailVerificationDetailers().add(emailVerificationDetailer);
        return userServiceRepo.save(user);
    }
    //This method create emailVerificationDetailer object.
    private EmailVerificationDetailer createEmailVerificationDetailer(User user){
        EmailVerificationDetailer emailVerificationDetailer = new EmailVerificationDetailer();
        emailVerificationDetailer.setUser(user);
        emailVerificationDetailer.setToken(jwtService.generateEmailVerificationToken(user));
        emailVerificationDetailer.setCreatedTimeStamp(new Timestamp(System.currentTimeMillis()));

        return emailVerificationDetailer;
    }
    public LoginResponseBody loginUser(LoginRequestBody loginRequestBody) throws UserNotFoundException, WrongPasswordException, UserNotVerifiedException, CouldNotSentEmailException {
        Optional<User> user = userServiceRepo.findByEmailIdIgnoreCase(loginRequestBody.getEmail());
        if(user.isPresent()){
            if(encryptionService.checkEncryptedPass(loginRequestBody.getPassword(),
                    user.get().getPassword())){
                if(user.get().getIsEmailverified()) {
                    String jwt = jwtService.generateJWT(user.get());
                    LoginResponseBody loginResponseBody = new LoginResponseBody();
                    loginResponseBody.setJwt(jwt);
                    return loginResponseBody;
                }else{
                    List<EmailVerificationDetailer> verificationTokenList = user.get().getEmailVerificationDetailers();
                    //Resend variable is to check if we want to resend the email or not. It is based on 2 things :
                    //a) If email verification was never sent to User.
                    //b) If it's been more than hour since the last email verification.
                    boolean resend = verificationTokenList.isEmpty() ||
                            verificationTokenList.get(0).getCreatedTimeStamp().before(new Timestamp(System.currentTimeMillis()-(3600*1000)));
                    if(resend){
                        //Create token
                        EmailVerificationDetailer emailVerificationDetailer = createEmailVerificationDetailer(user.get());
                        //Send mail
                        emailService.sendVerificationEmail(emailVerificationDetailer);
                        //Save the verification token email details to Db.
                        emailVerificationRepo.save(emailVerificationDetailer);
                    }
                    // resend is passed to the exception class for more clarity about whether the email is sent or not.
                    // If none of the above condition is followed then resend = false.
                    throw new UserNotVerifiedException(resend);
                }
            }
            else{
                throw new WrongPasswordException("Wrong password.");
            }
        }
        else{
            throw new UserNotFoundException("Email id is not registered. Please register");
        }
    }

    public String requestPasswordResetLink(LoginRequestBody loginRequestBody) throws CouldNotSentEmailException, UserNotFoundException, WrongPasswordException {
        Optional<User> opUser = userServiceRepo.findByEmailIdIgnoreCase(loginRequestBody.getEmail());
        if(!opUser.isPresent()) {
            throw new UserNotFoundException("user not present");
        }

        User user = opUser.get();
        if(!encryptionService.checkEncryptedPass(loginRequestBody.getPassword(),user.getPassword())){
            throw new WrongPasswordException("wrong password.");
        }

        String token = jwtService.generatePasswordResetVerificationToken(user);
        emailService.sendPasswordResetLink(user,token);
        return "Password reset link sent successfully";
    }

    public String resetPassword(String token,String password) throws UserNotFoundException {
        String email = jwtService.getEmailForPasswordReset(token);
        Optional<User> opUser = userServiceRepo.findByEmailIdIgnoreCase(email);
        if(!opUser.isPresent()) {
            throw new UserNotFoundException("User not present");
        }

        User user = opUser.get();
        user.setPassword(encryptionService.encryptPassword(password));
        userServiceRepo.save(user);
        return "Password reset successfully.";
    }
    @Transactional
    public boolean verifyToken(String token) throws CouldNotSentEmailException {
        Optional<EmailVerificationDetailer> opVerificationDetailer = emailVerificationRepo.findByToken(token);
        //Check if token is associated to any object.
        if(opVerificationDetailer.isPresent()){
            EmailVerificationDetailer emailVerificationDetailer = opVerificationDetailer.get();
            User user = emailVerificationDetailer.getUser();
            //Check if the user that is associated to the token is verified or not.
            if(!user.getIsEmailverified()){
                user.setIsEmailverified(true);
                userServiceRepo.save(user);
                emailVerificationRepo.deleteByUser(user);
                return true;
            }
        }
        return false;
    }
}
