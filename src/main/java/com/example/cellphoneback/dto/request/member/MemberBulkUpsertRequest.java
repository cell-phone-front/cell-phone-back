package com.example.cellphoneback.dto.request.member;

import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.entity.member.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Schema(description = "회원 일괄 등록/수정 요청 DTO")
public class MemberBulkUpsertRequest {

    @Schema(description = "회원 목록", requiredMode = Schema.RequiredMode.REQUIRED)
    List<Item> memberList;

    @Getter
    @Setter
    @Schema(description = "회원 항목")
    static public class Item {

        @Schema(description = "회원 ID", example = "user01")
        private String id;

        @Schema(description = "이름", example = "홍길동")
        private String name;

        @Schema(description = "이메일", example = "user01@example.com")
        private String email;

        @Schema(description = "전화번호", example = "010-1234-5678")
        private String phoneNumber;

        @Schema(description = "부서", example = "생산팀")
        private String dept;

        @Schema(description = "근무 팀", example = "A조")
        private String workTeam;

        @Schema(description = "권한 (ADMIN, PLANNER, WORKER 등)", example = "WORKER")
        private String role;

        @Schema(description = "입사일", example = "2026-01-01")
        private String hireDate;

        public Member toEntity() {
            String requestId = this.id;
            if(requestId == null || requestId.isBlank()) {
                requestId = UUID.randomUUID().toString().substring(0, 6);
            }
            return Member.builder()
                    .id(requestId)
                    .name(this.name)
                    .email(this.email)
                    .phoneNumber(this.phoneNumber)
                    .dept(this.dept)
                    .workTeam(this.workTeam)
                    .role(Role.valueOf(this.role == null ? "" : this.role.trim().toUpperCase()))
                    .hireDate(this.hireDate)
                    .build();
        }
    }
}