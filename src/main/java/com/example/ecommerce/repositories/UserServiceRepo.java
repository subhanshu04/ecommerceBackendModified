package com.example.ecommerce.repositories;


import com.example.ecommerce.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserServiceRepo extends JpaRepository<User,Long> {
    @Modifying
    @Transactional
    void deleteByName(String name);
    Optional<User> findByNameIgnoreCase(String name);

    Optional<User> findByEmailIdIgnoreCase(String emailId);

    @org.springframework.transaction.annotation.Transactional
    @Modifying
    @Query("update User u set u.password = ?1 where u.emailId = ?2")
    int updatePasswordByEmailid(String password, String emailId);


}
