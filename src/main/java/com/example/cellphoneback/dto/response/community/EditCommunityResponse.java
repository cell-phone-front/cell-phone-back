package com.example.cellphoneback.dto.response.community;

import com.example.cellphoneback.entity.community.Community;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "커뮤니티 수정 응답 DTO")
public class EditCommunityResponse {

    @Schema(description = "커뮤니티 제목", example = "업데이트된 제목")
    private String title;

    @Schema(description = "커뮤니티 내용", example = "업데이트된 내용입니다.")
    private String content;

    public static EditCommunityResponse fromEntity(Community community) {
        return EditCommunityResponse.builder()
                .title(community.getTitle())
                .content(community.getContent())
                .build();
    }
}