package com.example.ecommerce.controllers;

import com.example.ecommerce.exceptions.NoProductsException;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/getAll")
    public ResponseEntity getProduct() throws NoProductsException {
        try{
            return new ResponseEntity(productService.getAllProducts(), HttpStatus.OK);
        } catch (NoProductsException e) {
            throw new NoProductsException("There is no product present");
        }
    }


}
