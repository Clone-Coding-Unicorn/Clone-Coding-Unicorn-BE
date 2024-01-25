package com.sparta.clonecodingunicorn.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.sparta.clonecodingunicorn.domain.member.dto.LoginRequestDto;
import com.sparta.clonecodingunicorn.domain.member.dto.LoginSuccessResponseDto;
import com.sparta.clonecodingunicorn.global.security.MemberDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/login");
    }

    @Override
    public org.springframework.security.core.Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws org.springframework.security.core.AuthenticationException {
        log.info("로그인 시도");
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getEmail(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공 및 JWT 생성");

        String email = ((MemberDetailsImpl) authResult.getPrincipal()).getUsername();
        // 로그인 성공 시 JWT 토큰 생성
        String token = jwtUtil.createToken(email);
        LoginSuccessResponseDto loginSuccessResponseDto = new LoginSuccessResponseDto(token);
        // JWT 토큰을 Cookie에 추가
        jwtUtil.addJwtToCookie(token, response);

        // JWT 토큰을 Header에 추가(기존)
//        jwtUtil.addJwtToHeader(tokenValue, response);

        response.getWriter().write(new ObjectMapper().writeValueAsString(loginSuccessResponseDto));
        response.setStatus(200);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패 {}", failed.getMessage());

        response.setStatus(401);
    }
}