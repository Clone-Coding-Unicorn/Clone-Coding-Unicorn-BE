package com.sparta.clonecodingunicorn.domain.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.clonecodingunicorn.domain.member.dto.SignupRequestDto;
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
    private final KakaoService kakaoService;

    private final JwtUtil jwtUtil;

    public MemberController(MemberService memberService, KakaoService kakaoService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.kakaoService = kakaoService;
        this.jwtUtil = jwtUtil;
    }

    // .1 가입
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid SignupRequestDto requestDto, BindingResult bindingResult) {
        return memberService.signup(requestDto, bindingResult);
    }

    /**
     * test url: https://kauth.kakao.com/oauth/authorize?client_id=671600df764bdd2a2198446875a85121&redirect_uri=http://localhost:8080/api/kakao/callback&response_type=code
     * case 1) Cookie로 'Authorization' + 'Bearer 제외 tokenValue'를 클라이언트에 전달하는 경우
     * case 2) header로 'Authorization' + 'Bearer 제외 tokenValue'를 클라이언트에 전달하는 경우
     */

    // 2-1. case 1) kakao 로그인
//    @GetMapping("/kakao/callback")
//    public String kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
//        String token = kakaoService.kakaoLogin(code);
//        String tokenValue = token.substring(7);
//        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, tokenValue);
//        cookie.setPath("/");
//        response.addCookie(cookie);
//        return "redirect:/";

    // 2-2. case 2) kakao 로그인
    @GetMapping("/kakao/callback")
    public ResponseEntity<String> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        String token = kakaoService.kakaoLogin(code);
        String tokenValue = token.substring(7);
//        jwtUtil.addJwtToHeader(tokenValue, response);
        jwtUtil.addJwtToCookie(tokenValue,response);
        return ResponseEntity.ok("로그인이 완료되었습니다.");
    }
}