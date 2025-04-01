package com.sk.skala.myapp.dto.request;

import lombok.Getter;

@Getter
public class BuyStockRequest {
    // private String playerId;
    private int stockIndex;
    private int quantity;
}
