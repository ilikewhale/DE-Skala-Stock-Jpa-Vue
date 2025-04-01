package com.sk.skala.myapp.dto.request;

import lombok.Getter;

@Getter
public class CreatePlayerRequest {
    private String playerId;
    private String playerPassword;
    private int playerMoney;
}
