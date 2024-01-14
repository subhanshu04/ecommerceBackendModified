package com.example.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;
    @JsonIgnore
    @ManyToOne
    private User user;
    private String city;
    private String country;
    private String addressLine;
    private long postalCode;
}
