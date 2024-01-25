package com.sparta.clonecodingunicorn.domain.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.clonecodingunicorn.domain.member.dto.GoogleMemberInfoDto;
import com.sparta.clonecodingunicorn.domain.member.entity.Member;
import com.sparta.clonecodingunicorn.domain.member.repository.MemberRepository;
import com.sparta.clonecodingunicorn.global.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Slf4j(topic = "[Google 로그인]")
@Service
public class GoogleService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;

    public GoogleService(PasswordEncoder passwordEncoder, MemberRepository memberRepository, RestTemplate restTemplate, JwtUtil jwtUtil) {
        this.passwordEncoder = passwordEncoder;
        this.memberRepository = memberRepository;
        this.restTemplate = restTemplate;
        this.jwtUtil = jwtUtil;
    }

    @Value("${google.client.id}")
    private String clientId;
    @Value("${google.secret.password}")
    private String secretPasswrod;
    @Value("${google.redirect.uri.test}")
    private String redirectUrl;

    public String googleLogin(String code) throws JsonProcessingException {
        // 1. 인증 코드로 구글 서버에 토큰 전달
        String accessToken = getGoogleToken(code);
        // 2. 구글 서버로부터 받은 토큰으로 API 호출(사용자 정보 요청)
        GoogleMemberInfoDto googleMemberInfo = getGoogleMemberInfo(accessToken);
        // 3. 필요에 따라 회원가입 진행 -> JWT 토큰 반환
        Member googleMember = registerGoogleMemberIfNeeded(googleMemberInfo);
        String token = jwtUtil.createToken(googleMember.getName());
        return token;
    }

    // 1. 인증 코드로 구글 서버에 토큰 전달
    private String getGoogleToken(String code) throws JsonProcessingException {
        // 1-1. 토큰 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://oauth2.googleapis.com/token")
                .encode()
                .build()
                .toUri();

        // 1-2. HTTP header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 1-3. HTTP body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("client_secret", secretPasswrod);
        body.add("redirect_uri", redirectUrl);
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(body);

        // 1-4. HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        // 1-5. HTTP 응답(JSON) -> access token 파싱
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        return jsonNode.get("access_token").asText();
    }

    // 2. 구글 서버로부터 받은 토큰으로 API 호출(사용자 정보 요청)
    private GoogleMemberInfoDto getGoogleMemberInfo(String accessToken) throws JsonProcessingException {
        // 2-1. 사용자 정보 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://www.googleapis.com/oauth2/v2/userinfo")
                .queryParam("access_token", accessToken)
                .encode()
                .build()
                .toUri();

        // 2-2. HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        // 2-3. HTTP 응답(JSON) -> 사용자 정보 파싱
        GoogleMemberInfoDto googleMemberInfo = new ObjectMapper().readValue(response.getBody(), GoogleMemberInfoDto.class);
        return googleMemberInfo;
    }

    // 3. 필요에 따라 회원가입 진행
    private Member registerGoogleMemberIfNeeded(GoogleMemberInfoDto googleMemberInfo) {
        String googleId = googleMemberInfo.getId();
        Member googleMember = memberRepository.findByGoogleId(googleId).orElse(null);

        // 3-1. NEWNEEK에서 Google 로그인을 하지 않은 경우 -> DB 내 Google ID 없음
        if (googleMember == null) {
            String googleEmail = googleMemberInfo.getEmail();
            Member sameEmailMember = memberRepository.findByEmail(googleEmail).orElse(null);
            // Google email과 동일한 email을 가진 멤버가 DB에 존재하는 경우
            if (sameEmailMember != null) {
                // NEWNEEK에서 가입한 정보로 Google 사용자 정보 대체 + Google ID 업데이트 진행
                googleMember = sameEmailMember;
                googleMember = googleMember.googleIdUpdate(googleId);
            } else {
                // 신규로 가입하는 경우
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);
                String email = googleMemberInfo.getEmail();
                String name = googleMemberInfo.getName();
                googleMember = new Member(email, encodedPassword, name, googleId);
            }
            memberRepository.save(googleMember);
        }
        // 3-2. NEWNEEK에서 Google 로그인을 한 경우 -> DB 내 Google ID 있음
        return googleMember;
    }
}