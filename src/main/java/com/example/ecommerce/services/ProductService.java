package com.example.ecommerce.services;

import com.example.ecommerce.dtos.ProductResponseBody;
import com.example.ecommerce.exceptions.NoProductsException;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repositories.ProductServiceRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private ProductServiceRepo productServiceRepo;

    public ProductService(ProductServiceRepo productServiceRepo) {
        this.productServiceRepo = productServiceRepo;
    }

    public List<ProductResponseBody> getAllProducts() throws NoProductsException {
        List<Product> products =  productServiceRepo.findAll();
        if(products.isEmpty()){
            throw new NoProductsException("Products not available");
        }
        List<ProductResponseBody> productResponseBodies = new ArrayList<>();
        for(Product product : products){
            ProductResponseBody prb = new ProductResponseBody();
            prb.setProductId(product.getProductId());
            prb.setProductName(product.getProductName());
            prb.setQuantityInStock(product.getQuantity().getQuantity());
            prb.setPrice(product.getPrice());
            productResponseBodies.add(prb);
        }
        return productResponseBodies;
    }
}
