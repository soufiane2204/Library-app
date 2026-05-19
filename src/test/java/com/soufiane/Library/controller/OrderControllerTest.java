package com.soufiane.Library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soufiane.Library.DTO.CartRequest;
import com.soufiane.Library.model.*;
import com.soufiane.Library.repository.BookRepo;
import com.soufiane.Library.repository.CartRepo;
import com.soufiane.Library.repository.OrderRepo;
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
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private OrderRepo orderRepo;

    private Book book;
    private Cart cart;

    @BeforeEach
    void setUp() {
        // clean all tables before each test
        orderRepo.deleteAll();
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


        cart = new Cart();
        cartRepo.save(cart);
    }

    private void addBookToCart() throws Exception {
        CartRequest request = new CartRequest();
        request.setCartId(cart.getCartId());
        request.setBookId(54);
        request.setQuantity(2);

        mockMvc.perform(post("/cart/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }

    // ---- POST /orders/checkout --------

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void checkout_ShouldReturn201_WhenCartHasItems() throws Exception {


        addBookToCart();

        mockMvc.perform(post("/orders/checkout")
                        .param("cartId", String.valueOf(cart.getCartId())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.items.length()").value(1))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.totalPrice").value(91.98));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void checkout_ShouldReturn400_WhenCartIsEmpty() throws Exception {

        mockMvc.perform(post("/orders/checkout")
                        .param("cartId", String.valueOf(cart.getCartId())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void checkout_ShouldReturn403_WhenNotAuthenticated() throws Exception {

        mockMvc.perform(post("/orders/checkout")
                        .param("cartId", String.valueOf(cart.getCartId())))
                .andExpect(status().isForbidden());
    }

    // -----  GET /orders --------

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void getAllOrders_ShouldReturn200_WhenAdmin() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void getAllOrders_ShouldReturn403_WhenUser() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllOrders_ShouldReturn403_WhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isForbidden());
    }

    // ------- GET /orders/{id} ------------

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void getOrderById_ShouldReturn200_WhenOrderExists() throws Exception {


        addBookToCart();

        String response = mockMvc.perform(post("/orders/checkout")
                        .param("cartId", String.valueOf(cart.getCartId())))
                .andReturn()
                .getResponse()
                .getContentAsString();

        int orderId = objectMapper.readTree(response).get("orderId").asInt();

        mockMvc.perform(get("/orders/" + orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(orderId));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void getOrderById_ShouldReturn404_WhenOrderNotFound() throws Exception {
        mockMvc.perform(get("/orders/999"))
                .andExpect(status().isNotFound());
    }

    // ----- PUT /orders/{id}/status --------

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void updateOrderStatus_ShouldReturn200_WhenAdmin() throws Exception {

        addBookToCart();

        String response = mockMvc.perform(post("/orders/checkout")
                        .param("cartId", String.valueOf(cart.getCartId())))
                .andReturn()
                .getResponse()
                .getContentAsString();

        int orderId = objectMapper.readTree(response).get("orderId").asInt();

        mockMvc.perform(put("/orders/" + orderId + "/status")
                        .param("status", "CONFIRMED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void updateOrderStatus_ShouldReturn403_WhenUser() throws Exception {

        mockMvc.perform(put("/orders/1/status")
                        .param("status", "CONFIRMED"))
                .andExpect(status().isForbidden());
    }
}