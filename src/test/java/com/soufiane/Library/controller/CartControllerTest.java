package com.soufiane.Library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soufiane.Library.DTO.CartRequest;
import com.soufiane.Library.model.Book;
import com.soufiane.Library.model.Cart;
import com.soufiane.Library.repository.BookRepo;
import com.soufiane.Library.repository.CartRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private BookRepo bookRepo;

    private Book book;
    private Cart cart;

    @BeforeEach
    void setUp() {
        cartRepo.deleteAll();
        bookRepo.deleteAll();

        book = new Book();
        book.setBookId(54);
        book.setBookName("Clean Code");
        book.setBookAuthor("Robert C. Martin");
        book.setAvailable(true);
        book.setPrice(45.99);
        book.setQuantity(10);
        book.setReleaseDate(new java.util.Date(108, 7, 1));
        bookRepo.save(book);

        // create and save a cart
        cart = new Cart();
        cartRepo.save(cart);
    }

    // ----- POST /cart --------

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void createCart_ShouldReturn201() throws Exception {
        mockMvc.perform(post("/cart"))
                .andExpect(status().isCreated());
    }

    @Test
    void createCart_ShouldReturn403_WhenNotAuthenticated() throws Exception {
        mockMvc.perform(post("/cart"))
                .andExpect(status().isForbidden());
    }

    // ---- GET /cart/{id} ----------

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void getCart_ShouldReturn200_WhenCartExists() throws Exception {
        mockMvc.perform(get("/cart/" + cart.getCartId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartId").value(cart.getCartId()));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void getCart_ShouldReturn404_WhenCartNotFound() throws Exception {
        mockMvc.perform(get("/cart/999"))
                .andExpect(status().isNotFound());
    }

    // ------- POST /cart/add ---------

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void addBookToCart_ShouldReturn200_WhenBookAndCartExist() throws Exception {


        CartRequest request = new CartRequest();
        request.setCartId(cart.getCartId());
        request.setBookId(54);
        request.setQuantity(2);

        mockMvc.perform(post("/cart/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(1));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void addBookToCart_ShouldReturn404_WhenBookNotFound() throws Exception {


        CartRequest request = new CartRequest();
        request.setCartId(cart.getCartId());
        request.setBookId(999);
        request.setQuantity(2);

        mockMvc.perform(post("/cart/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    // ---------- DELETE /cart/remove --------

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void removeBookFromCart_ShouldReturn200_WhenItemExists() throws Exception {


        CartRequest request = new CartRequest();
        request.setCartId(cart.getCartId());
        request.setBookId(54);
        request.setQuantity(2);

        mockMvc.perform(post("/cart/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        mockMvc.perform(delete("/cart/remove")
                        .param("cartId", String.valueOf(cart.getCartId()))
                        .param("bookId", "54"))
                .andExpect(status().isOk());
    }

    // ------- DELETE /cart/clear ----------

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void clearCart_ShouldReturn200() throws Exception {
        mockMvc.perform(delete("/cart/clear/" + cart.getCartId()))
                .andExpect(status().isOk());
    }

    // ---- GET /cart/{id}/total ----------

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void getCartTotal_ShouldReturn200_WithCorrectTotal() throws Exception {


        CartRequest request = new CartRequest();
        request.setCartId(cart.getCartId());
        request.setBookId(54);
        request.setQuantity(2);

        mockMvc.perform(post("/cart/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // total should be 45.99 * 2 = 91.98
        mockMvc.perform(get("/cart/" + cart.getCartId() + "/total"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(91.98))
                .andExpect(jsonPath("$.itemCount").value(1));
    }
}