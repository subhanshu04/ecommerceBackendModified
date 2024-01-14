package com.example.ecommerce.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseBody {
    private Long productId;
    private String productName;
    private Double price;
    private int quantityInStock;
}
