package com.example.ecommerce.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressBody {
    private Long addressId;
    private String city;
    private String country;
    private String addressLine;
    private long postalCode;
}
