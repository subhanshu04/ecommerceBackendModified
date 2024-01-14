package com.example.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Quantity {
    @Id
    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;
    private int quantity;
}
