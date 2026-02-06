package com.example.cellphoneback.dto.response.community;

import com.example.cellphoneback.entity.community.Community;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "커뮤니티 ID로 조회한 응답 DTO")
public class SearchCommunityByIdResponse {

    @Schema(description = "커뮤니티 ID", example = "1")
    private Integer id;

    @Schema(description = "작성자 회원 ID", example = "user123")
    private String memberId;

    @Schema(description = "커뮤니티 제목", example = "새로운 기능 안내")
    private String title;

    @Schema(description = "커뮤니티 내용", example = "이번 업데이트에서는...")
    private String content;

    @Schema(description = "작성일시", example = "2026-02-06T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "댓글 수", example = "5")
    private int commentCount;

    @Schema(description = "조회 수", example = "150")
    private int viewCount;

    public static SearchCommunityByIdResponse fromEntity(Community community){
        return SearchCommunityByIdResponse.builder()
                .id(community.getId())
                .memberId(community.getMember().getId())
                .title(community.getTitle())
                .content(community.getContent())
                .createdAt(community.getCreatedAt())
                .commentCount(community.getCommentCount())
                .viewCount(community.getViewCount())
                .build();
    }
}