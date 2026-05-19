package com.soufiane.Library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soufiane.Library.model.Book;
import com.soufiane.Library.repository.BookRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc

@ActiveProfiles("test")
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepo bookRepo;
    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setBookId(1);
        book.setBookName("Clean Code");
        book.setBookAuthor("Robert C. Martin");
        book.setAvailable(true);
        book.setPrice(45.99);
        book.setQuantity(10);
        book.setReleaseDate(new Date(2008 - 1900, 7, 1));
    }

    // ----- GET /books -----
    @Test
    void getAllBooks_ShouldReturn200_WhenBooksExist() throws Exception {
        bookRepo.save(book);
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookName").value("Clean Code"));

    }

    @Test
    void getBookById_ShouldReturn200_WhenBookExists() throws Exception {
        bookRepo.save(book);
        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookName").value("Clean Code"))
                .andExpect(jsonPath("$.bookAuthor").value("Robert C. Martin"));

    }

    @Test
    void getBookById_ShouldReturn404_WhenBookNotFound() throws Exception {
        mockMvc.perform(get("/books/99"))
                .andExpect(status().isNotFound());
    }


    //-----POST /books ----------
    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void addBook_ShouldReturn201_WhenAdminAddsBook() throws Exception {

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bookName").value("Clean Code"));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void addBook_ShouldReturn403_WhenUserAddsBook() throws Exception {

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isForbidden());
    }

    @Test
    void addBook_ShouldReturn403_WhenNotAuthenticated() throws Exception {

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isForbidden());
    }

    // ----PUT /books --------------
    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void updateBook_ShouldReturn200_WhenAdminUpdatesBook() throws Exception {

        bookRepo.save(book);
        book.setBookName("Clean Code Updated");

        mockMvc.perform(put("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookName").value("Clean Code Updated"));

    }

    //-------DELETE /books ----------
    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void deleteBook_ShouldReturn200_WhenAdminDeletesBook() throws Exception {

        bookRepo.save(book);

        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void deleteBook_ShouldReturn403_WhenUserTriesToDelete() throws Exception {

        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isForbidden());
    }
}












