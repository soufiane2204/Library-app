package com.soufiane.Library.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartRequest {
    private int cartId;
    private int bookId;
    private int quantity;
}
