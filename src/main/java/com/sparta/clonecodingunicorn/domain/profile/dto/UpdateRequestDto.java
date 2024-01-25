package com.sparta.clonecodingunicorn.domain.profile.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateRequestDto {
    private String name;
    private Integer birthYear;
    private String gender;
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    private String emoji;
    private String job;
    private String interestArea;
    private Boolean deleteMember;
    private Boolean subscribeAgree;
}
