package com.example.cellphoneback.dto.response.member;

import com.example.cellphoneback.entity.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "회원 리스트 응답 DTO")
public class MemberListResponse {

    @Schema(description = "회원 목록")
    private List<Member> memberList;
}