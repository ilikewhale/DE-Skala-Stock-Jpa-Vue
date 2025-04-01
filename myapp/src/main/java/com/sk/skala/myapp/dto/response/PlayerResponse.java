package com.sk.skala.myapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlayerResponse {
    private String playerId;
    private int money;
}
