package com.example.cellphoneback.dto.response.notice;
import com.example.cellphoneback.entity.notice.Notice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "공지사항 목록 항목 DTO")
public class NoticeListResponse {

        @Schema(description = "공지사항 ID", example = "12")
        private int id;

        @Schema(description = "작성자 이름", example = "홍길동")
        private String memberName;

        @Schema(description = "공지사항 제목", example = "시스템 점검 안내")
        private String title;

        @Schema(description = "작성일", example = "2026-02-14T09:30:00")
        private LocalDateTime createdAt;

        @Schema(description = "핀 고정 여부", example = "true")
        private boolean pinned;

        @Schema(description = "조회수", example = "128")
        private int viewCount;

        public static NoticeListResponse fromEntity(Notice n) {
            return NoticeListResponse.builder()
                    .id(n.getId())
                    .memberName(n.getMember().getName())
                    .title(n.getTitle())
                    .createdAt(n.getCreatedAt())
                    .pinned(Boolean.TRUE.equals(n.getPinned()))
                    .viewCount(n.getViewCount())
                    .build();
        }


}
