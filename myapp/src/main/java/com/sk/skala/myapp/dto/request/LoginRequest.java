package com.sk.skala.myapp.dto.request;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String playerId;
    private String playerPassword;
}
