package com.sk.skala.myapp.dto.request;

import lombok.Getter;

@Getter
public class SellStockRequest {
    private int stockIndex;
    private int quantity;
}
