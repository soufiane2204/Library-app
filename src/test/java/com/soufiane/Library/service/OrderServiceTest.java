package com.soufiane.Library.service;

import com.soufiane.Library.exception.ResourceNotFoundException;
import com.soufiane.Library.model.*;
import com.soufiane.Library.repository.CartRepo;
import com.soufiane.Library.repository.OrderRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private CartRepo cartRepo;

    @InjectMocks
    private OrderService orderService;

    private Cart cart;
    private Book book;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setBookId(54);
        book.setBookName("Clean Code");
        book.setPrice(45.99);

        cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setQuantity(2);

        cart = new Cart();
        cart.setCartId(1);
        cart.getItems().add(cartItem);
    }

    // ─── checkout() tests ──────────────────────────────────────────

    @Test
    void checkout_ShouldCreateOrder_WhenCartHasItems() {

        when(cartRepo.findById(1)).thenReturn(Optional.of(cart));

        when(orderRepo.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(cartRepo.save(any(Cart.class))).thenReturn(cart);

        Order result = orderService.checkout(1);


        assertNotNull(result);

        assertEquals(1, result.getItems().size());

        assertEquals(OrderStatus.PENDING, result.getStatus());

        assertEquals(91.98, result.getTotalPrice());

        assertEquals(45.99, result.getItems().get(0).getPriceAtPurchase());

        assertEquals(0, cart.getItems().size());

        verify(orderRepo, times(1)).save(any(Order.class));
        verify(cartRepo, times(1)).save(any(Cart.class));
    }

    @Test
    void checkout_ShouldThrowException_WhenCartNotFound() {

        when(cartRepo.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            orderService.checkout(99);
        });

        verify(orderRepo, never()).save(any(Order.class));
    }

    @Test
    void checkout_ShouldThrowException_WhenCartIsEmpty() {

        Cart emptyCart = new Cart();
        emptyCart.setCartId(2);

        when(cartRepo.findById(2)).thenReturn(Optional.of(emptyCart));

        assertThrows(RuntimeException.class, () -> {
            orderService.checkout(2);
        });

        verify(orderRepo, never()).save(any(Order.class));
    }

    // ─── getOrderById() tests ──────────────────────────────────────

    @Test
    void getOrderById_ShouldReturnOrder_WhenOrderExists() {

        Order order = new Order();
        order.setOrderId(1);
        order.setStatus(OrderStatus.PENDING);

        when(orderRepo.findById(1)).thenReturn(Optional.of(order));

        Order result = orderService.getOrderById(1);

        assertNotNull(result);
        assertEquals(1, result.getOrderId());
        assertEquals(OrderStatus.PENDING, result.getStatus());
    }

    @Test
    void getOrderById_ShouldThrowException_WhenOrderNotFound() {

        when(orderRepo.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            orderService.getOrderById(99);
        });
    }

    // ─── getAllOrders() tests ──────────────────────────────────────

    @Test
    void getAllOrders_ShouldReturnAllOrders() {

        Order order1 = new Order();
        order1.setOrderId(1);

        Order order2 = new Order();
        order2.setOrderId(2);

        when(orderRepo.findAll()).thenReturn(List.of(order1, order2));

        var result = orderService.getAllOrders();

        assertEquals(2, result.size());

        verify(orderRepo, times(1)).findAll();
    }

    // ─── updateOrderStatus() tests ────────────────────────────────

    @Test
    void updateOrderStatus_ShouldUpdateStatus_WhenOrderExists() {

        // ARRANGE
        Order order = new Order();
        order.setOrderId(1);
        order.setStatus(OrderStatus.PENDING);

        when(orderRepo.findById(1)).thenReturn(Optional.of(order));

        when(orderRepo.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.updateOrderStatus(1, OrderStatus.CONFIRMED);

        assertEquals(OrderStatus.CONFIRMED, result.getStatus());
    }

    @Test
    void updateOrderStatus_ShouldThrowException_WhenOrderNotFound() {

        when(orderRepo.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            orderService.updateOrderStatus(99, OrderStatus.CONFIRMED);
        });
    }
}