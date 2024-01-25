package com.sparta.clonecodingunicorn.domain.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.clonecodingunicorn.domain.member.dto.SignupRequestDto;
import com.sparta.clonecodingunicorn.domain.member.service.GoogleService;
import com.sparta.clonecodingunicorn.domain.member.service.KakaoService;
import com.sparta.clonecodingunicorn.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class MemberController {
    private final MemberService memberService;
    private final KakaoService kakaoService;
    private final GoogleService googleService;

    public MemberController(MemberService memberService, KakaoService kakaoService, GoogleService googleService) {
        this.memberService = memberService;
        this.kakaoService = kakaoService;
        this.googleService = googleService;
    }

    // 가입
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid SignupRequestDto requestDto, BindingResult bindingResult) {
        return memberService.signup(requestDto, bindingResult);
    }

    // kakao 로그인
    @GetMapping("/kakao/callback")
    public ResponseEntity<String> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        return kakaoService.kakaoLogin(code, response);
    }

    // Google 로그인
    @GetMapping("/login/oauth2/code/google")
    public ResponseEntity<String> googleLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        return googleService.googleLogin(code, response);
    }
}