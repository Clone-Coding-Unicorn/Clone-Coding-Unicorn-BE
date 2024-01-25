package com.sparta.clonecodingunicorn.domain.member.dto;

import lombok.Getter;

@Getter
public class LoginSuccessResponseDto {
    private String token;

    public LoginSuccessResponseDto(String token) {
        this.token = token;
    }
}
