package com.example.ecommerce.repositories;

import com.example.ecommerce.model.EmailVerificationDetailer;
import com.example.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepo extends JpaRepository<EmailVerificationDetailer,Long> {
    Optional<EmailVerificationDetailer> findByToken(String token);

    void deleteByUser(User user);

}
