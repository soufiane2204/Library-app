package com.soufiane.Library.repository;

import com.soufiane.Library.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<Category,Integer> {
    Category findByCategoryNameIgnoreCase(String categoryName);
}
