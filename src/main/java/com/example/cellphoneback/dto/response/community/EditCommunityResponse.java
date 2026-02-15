package com.example.cellphoneback.dto.response.community;

import com.example.cellphoneback.entity.community.Community;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "커뮤니티 수정 응답 DTO")
public class EditCommunityResponse {

    @Schema(description = "커뮤니티 ID", example = "1")
    private int id;

    @Schema(description = "커뮤니티 제목", example = "업데이트된 제목")
    private String title;

    @Schema(description = "커뮤니티 내용", example = "업데이트된 내용입니다.")
    private String content;

    @Schema(description = "작성일시", example = "2026-02-06T10:15:30")
    private LocalDateTime createdAt;

    public static EditCommunityResponse fromEntity(Community community) {
        return EditCommunityResponse.builder()
                .id(community.getId())
                .title(community.getTitle())
                .content(community.getContent())
                .createdAt(community.getCreatedAt())
                .build();
    }
}