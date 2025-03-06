package com.example.ecommerce.controllers;

import com.example.ecommerce.dtos.PlacedOrderBody;
import com.example.ecommerce.exceptions.QuantityNotAvailableException;
import com.example.ecommerce.exceptions.WrongProductNameException;
import com.example.ecommerce.model.User;
import com.example.ecommerce.model.WebOrder;
import com.example.ecommerce.services.OrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping()
    public List<WebOrder> getOrders(@AuthenticationPrincipal User user) {
        return orderService.getOrders(user);
    }

    @PostMapping("/createOrder")
    public WebOrder createOrder(@AuthenticationPrincipal User user, @RequestBody PlacedOrderBody placedOrderBody)
            throws WrongProductNameException, QuantityNotAvailableException {
        return orderService.createOrder(user, placedOrderBody);
    }

    @PostMapping("/createOrderForDemo")
    public WebOrder createOrderForDemo(@AuthenticationPrincipal User user, @RequestBody PlacedOrderBody placedOrderBody)
            throws WrongProductNameException, QuantityNotAvailableException {
        return orderService.createOrder(user, placedOrderBody);
    }
}
