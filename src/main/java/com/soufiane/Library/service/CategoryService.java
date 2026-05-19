package com.soufiane.Library.service;

import com.soufiane.Library.exception.ResourceNotFoundException;
import com.soufiane.Library.model.Book;
import com.soufiane.Library.model.Category;
import com.soufiane.Library.repository.BookRepo;
import com.soufiane.Library.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private BookRepo bookRepo;

    public List<Category> getAllCategories(){
        List<Category> categories = categoryRepo.findAll();
        if(categories.isEmpty()){
            throw new RuntimeException("No categories found");
        }
        return categories;
    }
    public Category findCategoryById(int categoryId){
        return categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

    }
    public Category createCategory(Category category){
        if (category.getCategoryName()==null || category.getCategoryName().trim().isEmpty()){ //trim removes space ""
            throw new RuntimeException("Category name is empty");
        }
        return categoryRepo.save(category);
    }
    public void deleteCategory(int categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found " + categoryId));

        List<Book> books = bookRepo.findByCategory_CategoryNameIgnoreCase(category.getCategoryName());
        books.forEach(book -> book.setCategory(null));
        bookRepo.saveAll(books);

        categoryRepo.delete(category);

    }
    public List<Book> getBooksByCategory(String categoryName){
        List<Book> books = bookRepo.findByCategory_CategoryNameIgnoreCase(categoryName);
        if(books.isEmpty()){
            throw new RuntimeException("No books found in category : " + categoryName);
        }
        return books;
    }









}
