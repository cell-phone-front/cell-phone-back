package com.example.cellphoneback.dto.response.community;

import com.example.cellphoneback.entity.community.Community;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "커뮤니티 생성 응답 DTO")
public class CreateCommunityResponse {

    @Schema(description = "커뮤니티 ID", example = "1")
    private int id;

    @Schema(description = "커뮤니티 제목", example = "새 글 작성")
    private String title;

    @Schema(description = "커뮤니티 내용", example = "이번에 새로 작성된 글입니다.")
    private String content;

    @Schema(description = "작성일시", example = "2026-02-06T10:15:30")
    private LocalDateTime createdAt;

    public static CreateCommunityResponse fromEntity(Community community){
        return CreateCommunityResponse.builder()
                .id(community.getId())
                .title(community.getTitle())
                .content(community.getContent())
                .createdAt(community.getCreatedAt())
                .build();
    }
}