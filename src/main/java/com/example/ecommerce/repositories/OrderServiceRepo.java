package com.example.ecommerce.repositories;

import com.example.ecommerce.model.User;
import com.example.ecommerce.model.WebOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderServiceRepo extends JpaRepository<WebOrder,Long> {
    List<WebOrder> findByUser(User user);

}
