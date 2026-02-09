package com.example.cellphoneback.dto.response.member;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@Schema(description = "엑셀용 회원 목록 응답 DTO")
public class MemberParseResponse {

    @Schema(description = "회원 리스트")
    private List<Xls> memberList;

    @Getter
    @Builder
    @ToString
    @JsonPropertyOrder({"id", "name", "email", "phoneNumber", "dept", "workTeam", "role", "hireDate"})
    @Schema(description = "회원 정보")
    public static class Xls {

        @Schema(description = "회원 ID", example = "user123")
        private String id;

        @Schema(description = "회원 이름", example = "홍길동")
        private String name;

        @Schema(description = "회원 이메일", example = "user@example.com")
        private String email;

        @Schema(description = "전화번호", example = "010-1234-5678")
        private String phoneNumber;

        @Schema(description = "부서", example = "개발팀")
        private String dept;

        @Schema(description = "팀", example = "백엔드")
        private String workTeam;

        @Schema(description = "권한", example = "ROLE_ADMIN")
        private String role;

        @Schema(description = "입사일", example = "2023-01-15")
        private String hireDate;
    }
}