package com.example.ecommerce.dtos;

import com.example.ecommerce.model.Address;
import com.example.ecommerce.model.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlacedOrderBody {
    private String username;
    private String address;
    private List<ProductDetailDto> productDetailList;
}
