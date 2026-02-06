package com.example.cellphoneback.dto.response.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "회원 일괄 업서트 처리 결과 DTO")
public class MemberBulkUpsertResponse {

    @Schema(description = "생성된 회원 수", example = "5")
    private int createMember;

    @Schema(description = "삭제된 회원 수", example = "2")
    private int deleteMember;

    @Schema(description = "수정된 회원 수", example = "3")
    private int updateMember;
}