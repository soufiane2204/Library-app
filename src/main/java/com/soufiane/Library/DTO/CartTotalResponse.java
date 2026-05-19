package com.soufiane.Library.DTO;

import lombok.Getter;

@Getter
public class CartTotalResponse {


    private int cartId;
    private double total;
    private int itemCount;

    public CartTotalResponse(int cartId, double total, int itemCount) {
        this.cartId = cartId;
        this.total = total;
        this.itemCount = itemCount;
    }


}
