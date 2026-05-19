package com.soufiane.Library.service;

import com.soufiane.Library.DTO.StatsResponse;
import com.soufiane.Library.exception.DuplicateResourceException;
import com.soufiane.Library.exception.ResourceNotFoundException;
import com.soufiane.Library.model.Book;
import com.soufiane.Library.repository.BookRepo;
import com.soufiane.Library.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Comparator;
import java.util.List;

@Service
public class BookService {
    @Autowired
    BookRepo bookRepo;
    @Autowired
    CategoryRepo categoryRepo;

    public Page<Book> getBooksByPage(int page , int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("BookName"));
        return bookRepo.findAll(pageable);
    }

    public List<Book> getAllBooks() {
        return bookRepo.findAll();
    }


    public Book getBookById(int bookId) {
       return  bookRepo.findById(bookId)
               .orElseThrow(() -> new ResourceNotFoundException("Book with id: " + bookId + " not found!"));

    }



    public List<Book> searchBooks(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new RuntimeException("keyword is null or empty!");
        }
        List books  = bookRepo.searchByKeyword(keyword);
        if (books.isEmpty()) {
            throw  new ResourceNotFoundException("No books found with this keyword" + keyword);
        }
        return books;

    }


    public List<Book> getBooksByAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new RuntimeException("author is null or empty!");
        }
        List<Book> books = bookRepo.findByBookAuthorContainingIgnoreCase(author);
        if (books.isEmpty()) {
            throw  new ResourceNotFoundException("No books found with this author" + author);
        }
        return books;
    }


    public List<Book> getAvailableBooks(){
        List<Book> books = bookRepo.findByAvailable(true);
        if (books.isEmpty()) {
            throw  new ResourceNotFoundException("No books found");
        }
        return books;
    }

    public List<Book> getBooksByPriceRange(Double min, Double max){
        if (min < 0 || max < 0) {
            throw new RuntimeException("Price cannot be negative");
        }
        if (min > max) {
            throw new RuntimeException("Min price cannot be greater than max price");
        }
        List<Book> books = bookRepo.findByPriceBetween(min, max);
        if (books.isEmpty()) {
            throw  new ResourceNotFoundException("No books found");
        }
        return books;
    }


    public Book addBook(Book book) {
        if (bookRepo.existsById(book.getBookId())) {
            throw new DuplicateResourceException("Book already exists with id: " + book.getBookId());
        }
        return bookRepo.save(book);
    }


    public void deleteBookById(int id) {
        bookRepo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Book with id: " + id + " not found!"));
        bookRepo.deleteById(id);


    }


    public Book updateBook(Book book) {
        bookRepo.findById(book.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException(" NO Book with id: " + book.getBookId()));
        if (book.getCategory() != null) {
            categoryRepo.findById(book.getCategory().getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category Not found with id: " + book.getCategory().getCategoryId()));
        }

        return bookRepo.save(book);
    }

    public StatsResponse getStats(){
        List<Book> books = bookRepo.findAll();
        long totalBooks = books.size();
        long availableBooks = books.stream().filter(Book :: isAvailable).count();
        long outOfStockBooks = totalBooks-availableBooks;

        double totalInventoryValue =books.stream()
                .mapToDouble(book -> book.getPrice()*book.getQuantity())
                .sum();
        double averagePrice = books.stream()
                .mapToDouble(Book ::getPrice)
                .average()
                .orElse(0.0);

        String mostExpensiveBook = books.stream()
                .max(Comparator.comparingDouble(Book ::getPrice))
                .map(Book ::getBookName)
                .orElse("No books Found");
        totalInventoryValue = Math.round(totalInventoryValue * 100.0)/100.0;
        averagePrice = Math.round(averagePrice * 100.0)/100.0;
        return new StatsResponse(totalBooks,availableBooks,outOfStockBooks
                ,totalInventoryValue,averagePrice,mostExpensiveBook);
    }

    public List<Book> getBooksByCategory(String categoryName) {
        return bookRepo.findByCategory_CategoryNameIgnoreCase(categoryName);
    }
}
