package com.example.cellphoneback.dto.response.community;

import com.example.cellphoneback.entity.community.Community;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "커뮤니티 목록 항목 DTO")
public class CommunityListResponse {

        @Schema(description = "커뮤니티 ID", example = "1")
        private Integer id;

        @Schema(description = "작성자 ID", example = "user123")
        private String memberId;

        @Schema(description = "제목", example = "새 글")
        private String title;

        @Schema(description = "작성일시")
        private LocalDateTime createdAt;

        @Schema(description = "댓글 수")
        private int commentCount;

        @Schema(description = "조회 수")
        private int viewCount;

        public static CommunityListResponse fromEntity(Community c) {
            return CommunityListResponse.builder()
                    .id(c.getId())
                    .memberId(c.getMember().getId())
                    .title(c.getTitle())
                    .createdAt(c.getCreatedAt())
                    .commentCount(c.getCommentCount())
                    .viewCount(c.getViewCount())
                    .build();
        }


}
