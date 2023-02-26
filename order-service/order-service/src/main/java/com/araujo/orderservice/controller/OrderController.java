package com.araujo.orderservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @PostMapping("payment")
    public OrderModel payOrder(@RequestBody OrderModel order) {
        orderService.payOrder(order);
        return order;
    }

    @DeleteMapping("payment")
    public OrderModel cancelOrder(@RequestBody OrderModel order) {
        this.orderService.cancelOrder(order);
        return order;
    }

}
