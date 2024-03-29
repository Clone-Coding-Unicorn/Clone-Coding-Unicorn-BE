package com.sparta.clonecodingunicorn.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoMemberInfoDto {
    private Long id;
    private String name;
    private String email;

    public KakaoMemberInfoDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}