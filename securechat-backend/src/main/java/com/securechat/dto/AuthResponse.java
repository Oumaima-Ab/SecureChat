package com.securechat.dto;

import lombok.Getter;

@Getter
public class AuthResponse {

    private String token;
    private String type = "Bearer";

    public AuthResponse(String token) {
        this.token = token;
    }

}