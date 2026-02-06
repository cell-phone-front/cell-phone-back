package com.example.cellphoneback.dto.response.member;

import com.example.cellphoneback.entity.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "회원 로그인 응답 DTO")
public class MemberLoginResponse {

    @Schema(description = "회원 정보")
    private Member member;

    @Schema(description = "JWT 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
}