package com.soufiane.Library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @Positive(message = "Book id must be positive")
    private int bookId;

    @NotBlank(message = "book name is required")
    @Size(min = 2,max = 100,message = "Book name must be between 2 and 100 characters")
    private  String bookName;

    @NotBlank(message = "Author name is required")
    @Size(min = 2,max = 100,message = "Author name must be between 2 and 100 characters")
    private String bookAuthor;

    private boolean available;

    @Positive(message = "Price must be greater than 0")
    private double price;

    @Positive(message = "Quantity must be greater than 0")
    private int quantity;

    @Past(message = "Release date must be in the past")
    @Temporal(TemporalType.DATE)
    private Date releaseDate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private  Category category;


}
