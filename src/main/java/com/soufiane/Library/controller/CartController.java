package com.soufiane.Library.controller;

import com.soufiane.Library.DTO.CartRequest;
import com.soufiane.Library.DTO.CartTotalResponse;
import com.soufiane.Library.model.Cart;
import com.soufiane.Library.repository.CartRepo;
import com.soufiane.Library.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class CartController {
    @Autowired
    private CartService service ;
    @Autowired
    private CartRepo cartRepo;


    @PostMapping("/cart")
    public ResponseEntity <Cart> createCart(){

       return new ResponseEntity<>(service.createCart(), HttpStatus.CREATED);
    }
    @PostMapping("/cart/add")
    public ResponseEntity<Cart> addBookToCart(@RequestBody CartRequest cartRequest){
            Cart cart = service.addBookToCart(
                    cartRequest.getCartId(),
                    cartRequest.getBookId(),
                    cartRequest.getQuantity());
            return new ResponseEntity<>(cart, HttpStatus.OK);

    }
    @DeleteMapping("/cart/delete")
    public ResponseEntity<Object> deleteCart(@RequestParam int cartId){
            service.deleteCart(cartId);
            return new ResponseEntity<>("cart "+cartId+" deleted ", HttpStatus.OK);

    }


    @DeleteMapping("/cart/remove")
    public ResponseEntity<String> removeBookFromCart(@RequestParam int cartId , @RequestParam int bookId){
            service.removeBookFromCart(cartId,bookId);
            return new ResponseEntity<>("removed!", HttpStatus.OK);
    }

    @GetMapping("/cart/{id}")
    public ResponseEntity<Object> getCart(@PathVariable int id){
            return new ResponseEntity<>(service.getCart(id) ,  HttpStatus.OK);
    }

    @DeleteMapping("/cart/clear/{cartId}")
    public ResponseEntity<Object> clearCart(@PathVariable int  cartId){
        service.clearCart(cartId);
        return new ResponseEntity<>("cart " +cartId +" cleared successfully ",HttpStatus.OK);
    }

    @GetMapping("/cart/{cartId}/total")
    public ResponseEntity<CartTotalResponse> getCartTotal(@PathVariable int cartId){
        return new ResponseEntity<>(service.getCartTotal(cartId) , HttpStatus.OK);
    }

    @PutMapping("/cart/update")
    public  ResponseEntity<Cart> updateCartItemQuantity(
            @RequestParam int cartId,
            @RequestParam int quantity,
            @RequestParam int bookId ){
        return new ResponseEntity<>(service.updateCartItemQuantity(cartId, quantity ,bookId), HttpStatus.OK);

    }


}
