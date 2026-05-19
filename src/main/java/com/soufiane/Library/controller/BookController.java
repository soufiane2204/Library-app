package com.soufiane.Library.controller;

import com.soufiane.Library.model.Book;
import com.soufiane.Library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class BookController {
    @Autowired
    private BookService service;



    @GetMapping("/books/page")
    public ResponseEntity<Page<Book>> getBooksByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size)
    {
        return new ResponseEntity<>(service.getBooksByPage(page,size),HttpStatus.OK);

    }

    @GetMapping("/books")
    public ResponseEntity <List<Book> >getAllBooks(){
      return new  ResponseEntity<>(service.getAllBooks() , HttpStatus.OK) ;
    }


    @PostMapping("/books")
    public ResponseEntity<?> addBook( @Valid @RequestBody Book book){
      return new  ResponseEntity<>(service.addBook(book), HttpStatus.CREATED) ;
    }



    @PutMapping("/books")
    public ResponseEntity<Book> updateBook(@Valid @RequestBody Book book){
       return new ResponseEntity<>(service.updateBook(book),HttpStatus.OK) ;
    }

    @GetMapping("/search/books")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam String keyword){
        return new ResponseEntity<>(service.searchBooks(keyword),HttpStatus.OK) ;
    }

    @GetMapping("search/books/author")
    public ResponseEntity<List<Book>> getBooksByAuthor(@RequestParam String author){
        return new ResponseEntity<>(service.getBooksByAuthor(author),HttpStatus.OK) ;
    }

    @GetMapping("search/books/available")
    public ResponseEntity<List<Book>> getBooksByAvailable(){
        return new ResponseEntity<>(service.getAvailableBooks(),HttpStatus.OK) ;
    }

    @GetMapping("/search/books/price")
    public ResponseEntity<List<Book>> getBooksByPrice(@RequestParam Double min, @RequestParam Double max){
        return new ResponseEntity<>(service.getBooksByPriceRange(min,max),HttpStatus.OK) ;
    }
    @GetMapping("/books/{bookId}")
    public ResponseEntity<Book> getBookById(@PathVariable int bookId) {
        return  new ResponseEntity<>(service.getBookById(bookId), HttpStatus.OK) ;

    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<String> deleteBookById(@PathVariable int id){
        service.deleteBookById(id);
        return new ResponseEntity<>("Book deleted successfully", HttpStatus.OK) ;
    }
    @GetMapping("/search/books/category")
    public ResponseEntity<List<Book>> getBooksByCategory(@RequestParam String name) {
        return new ResponseEntity<>(service.getBooksByCategory(name), HttpStatus.OK);
    }




}
