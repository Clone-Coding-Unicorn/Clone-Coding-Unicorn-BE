package com.sparta.clonecodingunicorn.domain.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.clonecodingunicorn.domain.member.dto.SignupRequestDto;
import com.sparta.clonecodingunicorn.domain.member.service.GoogleService;
import com.sparta.clonecodingunicorn.domain.member.service.KakaoService;
import com.sparta.clonecodingunicorn.domain.member.service.MemberService;
import com.sparta.clonecodingunicorn.global.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
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
    private final GoogleService googleService;
    private final KakaoService kakaoService;

    public MemberController(MemberService memberService, GoogleService googleService, KakaoService kakaoService) {
        this.memberService = memberService;
        this.googleService = googleService;
        this.kakaoService = kakaoService;
    }

    // 가입
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid SignupRequestDto requestDto, BindingResult bindingResult) {
        return memberService.signup(requestDto, bindingResult);
    }

    // Google 로그인
    @GetMapping("/login/oauth2/code/google")
    public String googleLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        String token = googleService.googleLogin(code);
        String tokenValue = token.substring(7);
        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, tokenValue);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/";
    }

    // kakao 로그인
    @GetMapping("/kakao/callback")
    public String kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        String token = kakaoService.kakaoLogin(code);
        String tokenValue = token.substring(7);
        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, tokenValue);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/";
    }
}