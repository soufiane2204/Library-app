package com.soufiane.Library.repository;

import com.soufiane.Library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepo extends JpaRepository<Book, Integer> {

    @Query("SELECT b FROM Book b WHERE LOWER(b.bookName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(b.bookAuthor) LIKE LOWER(CONCAT('%', :keyword, '%'))") // java persistence query language
    List<Book> searchByKeyword(String keyword);

    List<Book> findByBookAuthorContainingIgnoreCase(String author);

    List<Book> findByAvailable(boolean available);

    List<Book> findByPriceBetween(Double min , Double max);

    List<Book> findByCategory_CategoryNameIgnoreCase(String categoryName);


}
