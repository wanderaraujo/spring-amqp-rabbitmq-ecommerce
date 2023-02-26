package com.araujo.orderservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.araujo.orderservice.model.OrderModel;
import com.araujo.orderservice.service.OrderService;

@RestController
@RequestMapping("/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public OrderModel create(@RequestBody OrderModel order) {
        orderService.createOrder(order);
        return order;
    }

}
