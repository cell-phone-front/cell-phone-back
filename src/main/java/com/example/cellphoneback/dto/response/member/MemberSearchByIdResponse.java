package com.example.cellphoneback.dto.response.member;

import com.example.cellphoneback.entity.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "회원 ID로 조회한 응답 DTO")
public class MemberSearchByIdResponse {

    @Schema(description = "회원 엔티티 객체")
    private Member member;
}