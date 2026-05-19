package com.soufiane.Library.controller;

import com.soufiane.Library.model.Order;
import com.soufiane.Library.model.OrderStatus;
import com.soufiane.Library.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/orders")
public class OrderController {

    @Autowired private OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<Order> checkout(@RequestParam int cartId) {
        return new ResponseEntity<>(orderService.checkout(cartId), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return new ResponseEntity<>(orderService.getAllOrders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable int id) {
        return new ResponseEntity<>(orderService.getOrderById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable int id,
            @RequestParam OrderStatus status) {
        return new ResponseEntity<>(orderService.updateOrderStatus(id,status),HttpStatus.OK);
    }
}