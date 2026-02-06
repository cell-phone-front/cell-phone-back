package com.example.cellphoneback.dto.response.notice;

import com.example.cellphoneback.entity.notice.Notice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "공지사항 생성 응답 DTO")
public class CreateNoticeResponse {

    @Schema(description = "공지사항 ID", example = "1")
    private int id;

    @Schema(description = "작성자 회원 ID", example = "user123")
    private String memberId;

    @Schema(description = "공지사항 제목", example = "서버 점검 안내")
    private String title;

    @Schema(description = "공지사항 내용", example = "내일 서버 점검이 예정되어 있습니다.")
    private String content;

    @Schema(description = "생성일시", example = "2026-02-06T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "상단 고정 여부", example = "true")
    private Boolean pinned;

    @Schema(description = "조회수", example = "150")
    private int viewCount;

    public static CreateNoticeResponse fromEntity(Notice notice){
        return CreateNoticeResponse.builder()
                .id(notice.getId())
                .memberId(notice.getMember().getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt())
                .pinned(notice.getPinned())
                .viewCount(notice.getViewCount())
                .build();
    }
}