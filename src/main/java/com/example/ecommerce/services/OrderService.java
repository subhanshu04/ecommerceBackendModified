package com.example.ecommerce.services;

import com.example.ecommerce.dtos.PlacedOrderBody;
import com.example.ecommerce.dtos.ProductDetailDto;
import com.example.ecommerce.exceptions.QuantityNotAvailableException;
import com.example.ecommerce.exceptions.WrongProductNameException;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.model.WebOrder;
import com.example.ecommerce.model.WebOrderQuantity;
import com.example.ecommerce.repositories.OrderServiceRepo;
import com.example.ecommerce.repositories.ProductServiceRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private OrderServiceRepo orderServiceRepo;
    private ProductServiceRepo productServiceRepo;

    public OrderService(OrderServiceRepo orderServiceRepo, ProductServiceRepo productServiceRepo) {
        this.orderServiceRepo = orderServiceRepo;
        this.productServiceRepo = productServiceRepo;
    }

    public List<WebOrder> getOrders(User user){
        List<WebOrder> orders = orderServiceRepo.findByUser(user);
        return orders;
    }

    public WebOrder createOrder(User user, PlacedOrderBody placedOrderBody) throws QuantityNotAvailableException, WrongProductNameException {
        //1 Check if the provided product name is correct or not.
        //2 check if the quantity is present or not.
        List<ProductDetailDto> productList = placedOrderBody.getProductDetailList();
        WebOrder webOrder = new WebOrder();
        webOrder.setUser(user);
        for(ProductDetailDto productDetail : productList){
            Optional<Product> opProduct = productServiceRepo.findByProductName(productDetail.getProductName());
            //Product Check
            if(opProduct.isPresent()){
                Product product = opProduct.get();
                //Quantity check
                if(product.getQuantity().getQuantity()>=productDetail.getQuantity()){
                    WebOrderQuantity webOrderQuantity = new WebOrderQuantity();
                    webOrderQuantity.setProduct(product);
                    webOrderQuantity.setQuantity(productDetail.getQuantity());
                    webOrder.getWebOrderQuantities().add(webOrderQuantity);
                }
                else{
                    //QuantityNotAvailableException
                    throw new QuantityNotAvailableException("Please select less quantity of "+product.getProductName());
                }
            }
            else{
                //WrongProductNameException
                throw new WrongProductNameException("The product name is not correct");
            }
        }
        orderServiceRepo.save(webOrder);
        return webOrder;
    }

}
