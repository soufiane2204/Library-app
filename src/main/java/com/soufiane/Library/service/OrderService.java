package com.soufiane.Library.service;

import com.soufiane.Library.exception.ResourceNotFoundException;
import com.soufiane.Library.model.*;
import com.soufiane.Library.repository.CartRepo;
import com.soufiane.Library.repository.OrderRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class OrderService {
    @Autowired private OrderRepo orderRepo;
    @Autowired private CartRepo cartRepo;

    @Transactional
    public Order checkout(int cartId) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("no cart with id" + cartId));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("no items in order");
        }
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(cartItem.getBook().getPrice());
            orderItem.setOrder(order);
            order.getItems().add(orderItem);
        }

        double total = order.getItems().stream()
                .mapToDouble(item -> item.getPriceAtPurchase()*item.getQuantity())
                .sum();
        order.setTotalPrice(total);
        orderRepo.save(order);
        cart.getItems().clear();
        cartRepo.save(cart);

        return order;

    }

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    public Order getOrderById(int orderId) {
        return orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(" No order with id " + orderId));
    }

    public Order updateOrderStatus(int orderId, OrderStatus orderStatus) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(" No order with id " + orderId));
        order.setStatus(orderStatus);
        return orderRepo.save(order);
    }




}
