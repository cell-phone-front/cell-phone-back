package com.example.cellphoneback.dto.request.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "회원 로그인 요청 DTO")
public class MemberLoginRequest {

    @Schema(description = "회원 이름", example = "홍길동")
    private String name;

    @Schema(description = "회원 ID", example = "user01")
    private String id;
}