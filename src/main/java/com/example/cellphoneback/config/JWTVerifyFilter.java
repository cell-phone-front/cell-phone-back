package com.example.cellphoneback.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.repository.member.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.NoSuchElementException;


@Component
@RequiredArgsConstructor
public class JWTVerifyFilter extends OncePerRequestFilter {

    private final MemberRepository memberRepository;

    // 특정 경로와 특정 메소드를 필터에서 제외할지 판단하는 역할
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();

        return (uri.equals("/api/member/login"))
                || method.equals("OPTIONS") // 메소드 요청이 OPTIONS 라고 오면 필터를 거치지 않겠다.
                || uri.startsWith("/swagger-ui")
                || uri.startsWith("/v3/api-docs");  // OpenAPI JSON 제외
    }

    // 모든 요청에 대해 필터가 수행할 동작 ( 인증 토큰 확인(JWT), 요청 헤더 검증 )
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 메소드 요청이 "OPTIONS"와 같다면 필터 인증 코드를 거치지 않고
        // 인증필터에서는 그냥 넘기고 다른 필터가 있다면 그쪽으로 넘기고 없다면 컨트롤러로 넘긴다.
        if ("OPTIONS".equals(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        // 헤더 Authorization 값을 가져와서 authHeader에 대입
        String authHeader = request.getHeader("Authorization");

        // 만약 authHeader가 null이거나 Bearer 로 시작하지 않으면 401 에러 보냄
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalStateException("토큰이 없거나 형식이 틀립니다.");
        }

        // 앞에 있는"Bearer "를 제외한 나머지를 가져옴
        String token = authHeader.substring(7);

        // 토큰 발급자가 맞는지 비밀키가 맞는지 검증
        DecodedJWT jwt;
        try {
            jwt = JWT.require(Algorithm.HMAC256("phoneKey")).withIssuer("cellPhone").build().verify(token);
        } catch (JWTVerificationException exception) {
            throw new IllegalStateException("사용자 토큰이 일치하지 않습니다.");
        }

        // 인증시에 사용했던 계정의 ID를 설정해서 보냄
        String subject = jwt.getSubject();

        // 해당 직원이 리스트에 있는지 확인
        Member member = memberRepository.findById(subject).orElseThrow(() ->
                new NoSuchElementException("직원 정보가 없습니다."));

        //request.setAttribute("memberId", subject);
        request.setAttribute("member", member);


        filterChain.doFilter(request, response);
    }
}
