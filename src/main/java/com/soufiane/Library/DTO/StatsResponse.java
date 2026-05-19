package com.soufiane.Library.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StatsResponse {
    private long totalBooks;
    private long availableBooks;
    private long outOfStockBooks;
    private double totalInventoryValue;
    private double averagePrice;
    private String mostExpensiveBook;
}
