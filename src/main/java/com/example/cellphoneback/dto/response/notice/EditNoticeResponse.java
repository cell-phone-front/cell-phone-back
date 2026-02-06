package com.example.cellphoneback.dto.response.notice;

import com.example.cellphoneback.entity.notice.Notice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "공지사항 수정 응답 DTO")
public class EditNoticeResponse {

    @Schema(description = "공지사항 제목", example = "공지사항 제목 예시")
    String title;

    @Schema(description = "공지사항 내용", example = "공지사항 내용 예시")
    String content;

    @Schema(description = "공지사항 생성일", example = "2026-02-06T09:00:00")
    LocalDateTime createdAt;

    public static EditNoticeResponse fromEntity(Notice notice){
        return EditNoticeResponse.builder()
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt())
                .build();
    }
}