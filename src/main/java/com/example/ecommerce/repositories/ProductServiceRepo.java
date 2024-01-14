package com.example.ecommerce.repositories;

import com.example.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductServiceRepo extends JpaRepository<Product,Long> {
    Optional<Product> findByProductName(String productName);

}
