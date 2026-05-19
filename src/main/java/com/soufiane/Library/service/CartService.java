package com.soufiane.Library.service;

import com.soufiane.Library.DTO.CartTotalResponse;
import com.soufiane.Library.exception.ResourceNotFoundException;
import com.soufiane.Library.model.Book;
import com.soufiane.Library.model.Cart;
import com.soufiane.Library.model.CartItem;
import com.soufiane.Library.repository.BookRepo;
import com.soufiane.Library.repository.CartRepo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class CartService {
    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private BookRepo bookRepo;

    public  Cart createCart() {
       return cartRepo.save(new Cart());
    }


    public void deleteCart(int cartId){
       cartRepo.findById(cartId)
               .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id : " + cartId));
       cartRepo.deleteById(cartId);

    }


    public Cart addBookToCart(int cartId, int bookId,int quantity) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id : " + cartId));

        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id : " + bookId));


        CartItem item = new CartItem();

        item.setBook(book);
        item.setQuantity(quantity);
        item.setCart(cart);

        cart.getItems().add(item);
        return cartRepo.save(cart);
    }


    public Cart getCart(int cartId){

        return cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found: " + cartId));
    }


    public void removeBookFromCart(int cartId, int bookId) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found: " + cartId));
        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getBook().getBookId() == bookId)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Book not found in cart with id : " + bookId));
        cart.getItems().remove(cartItem);
        cartRepo.save(cart);

    }


    public void clearCart(int cartId) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found: " + cartId));
        cart.getItems().clear();
        cartRepo.save(cart);

    }


    public CartTotalResponse getCartTotal(int cartId) {
        Cart cart =cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found: " + cartId));

        double total = cart.getItems().stream()
                .mapToDouble(item -> item.getBook().getPrice()*item.getQuantity())
                .sum();
        int itemCount = cart.getItems().size();

        return new CartTotalResponse(cartId,total,itemCount);
    }


    public Cart updateCartItemQuantity(int cartId, int quantity , int bookId) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found: " + cartId));

        CartItem cartItem  = cart.getItems().stream()
                .filter(item -> item.getBook().getBookId()==bookId)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id : " + bookId));
        if(quantity<=0){
            throw new RuntimeException("Quantity must be greater than 0");
        }
        cartItem.setQuantity(quantity);
        return cartRepo.save(cart);
    }
}
