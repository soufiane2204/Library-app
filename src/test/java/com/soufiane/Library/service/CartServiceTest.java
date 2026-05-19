package com.soufiane.Library.service;

import com.soufiane.Library.exception.ResourceNotFoundException;
import com.soufiane.Library.model.Book;
import com.soufiane.Library.model.Cart;
import com.soufiane.Library.model.CartItem;
import com.soufiane.Library.repository.BookRepo;
import com.soufiane.Library.repository.CartRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepo cartRepo;
    @Mock
    private BookRepo bookRepo;


    @InjectMocks
    private CartService cartService;

    private Cart cart;
    private Book book;

    @BeforeEach
    void setUp() {
        cart = new Cart();
        cart.setCartId(1);

        book = new Book();
        book.setBookId(54);
        book.setBookName("Clean Code");
        book.setPrice(45.99);
    }


    //----getCart() tests -----
    @Test
    void getCart_ShouldReturnCart_WhenCartExists() {

        when(cartRepo.findById(1)).thenReturn(Optional.of(cart));

        Cart result = cartService.getCart(1);

        assertNotNull(result);
        assertEquals(1,result.getCartId());

        verify(cartRepo,times(1)).findById(1);

    }
    @Test
    void getCart_ShouldThrowResourceNotFoundException_WhenCartNotFound() {

        when(cartRepo.findById(99)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> cartService.getCart(99));
    }

    //-- addBookToCart() tests-------
    @Test
    void addBookToCart_shouldAddItem_WhenCartAndBookExists() {

        when(cartRepo.findById(1)).thenReturn(Optional.of(cart));
        when(bookRepo.findById(54)).thenReturn(Optional.of(book));

        when(cartRepo.save(cart)).thenReturn(cart);


        Cart result = cartService.addBookToCart(1,54,2);

        assertEquals(1,result.getItems().size());
        assertEquals(54,result.getItems().get(0).getBook().getBookId());
        assertEquals(2,result.getItems().get(0).getQuantity());

    }
    @Test
    void addBookToCart_ShouldThrowException_WhenBookNotFound() {

        when(cartRepo.findById(1)).thenReturn(Optional.of(cart));
        when(bookRepo.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,() -> cartService.addBookToCart(1,99,2));

    }

    //----- removeBookFromCart() tests -----
    @Test
    void removeBookFromCart_shouldRemoveItem_WhenItemExists() {
        CartItem  item = new CartItem();
        item.setBook(book);
        item.setQuantity(2);
        cart.getItems().add(item);

        when(cartRepo.findById(1)).thenReturn(Optional.of(cart));
        when(cartRepo.save(any(Cart.class))).thenReturn(cart);

        cartService.removeBookFromCart(1,54);

        assertEquals(0,cart.getItems().size());

    }
    @Test
    void removeBookFromCart_ShouldThrowException_WhenItemNotFound() {
        when(cartRepo.findById(1)).thenReturn(Optional.of(cart));
        assertThrows(ResourceNotFoundException.class,
                () -> cartService.removeBookFromCart(1,54));
    }

    //----- clearCart() tests ------
    @Test
    void clearCart_ShouldEmptyAllItems() {
        CartItem  item1 = new CartItem();
        item1.setBook(book);
        cart.getItems().add(item1);
        CartItem  item2 = new CartItem();
        item2.setBook(book);
        cart.getItems().add(item2);

        when(cartRepo.findById(1)).thenReturn(Optional.of(cart));
        when(cartRepo.save(any(Cart.class))).thenReturn(cart);

        cartService.clearCart(1);

        assertEquals(0,cart.getItems().size());
    }

    // ─── getCartTotal() tests ────────────────
    @Test
    void getCartTotal_ShouldReturnCorrectTotal() {
        CartItem  item = new CartItem();
        item.setBook(book);
        item.setQuantity(2);
        cart.getItems().add(item);

        when(cartRepo.findById(1)).thenReturn(Optional.of(cart));

        var result = cartService.getCartTotal(1);

        assertEquals(91.98, result.getTotal());
        assertEquals(1,result.getItemCount());
    }
    // ─── updateCartItemQuantity() tests ───────────────────────────────

    @Test
    void updateCartItemQuantity_ShouldUpdateQuantity_WhenItemExists() {

        CartItem item = new CartItem();
        item.setBook(book);
        item.setQuantity(2);
        cart.getItems().add(item);

        when(cartRepo.findById(1)).thenReturn(Optional.of(cart));
        when(cartRepo.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cart result = cartService.updateCartItemQuantity(1, 5, 54);

        assertEquals(5, result.getItems().get(0).getQuantity());
    }

    @Test
    void updateCartItemQuantity_ShouldThrowException_WhenQuantityIsZero() {

        CartItem item = new CartItem();
        item.setBook(book);
        item.setQuantity(2);
        cart.getItems().add(item);

        when(cartRepo.findById(1)).thenReturn(Optional.of(cart));

        assertThrows(RuntimeException.class, () -> {
            cartService.updateCartItemQuantity(1, 54, 0);
        });
    }

    @Test
    void updateCartItemQuantity_ShouldThrowException_WhenItemNotInCart() {

        when(cartRepo.findById(1)).thenReturn(Optional.of(cart));

        assertThrows(ResourceNotFoundException.class, () -> {
            cartService.updateCartItemQuantity(1, 54, 3);
        });
    }




}
