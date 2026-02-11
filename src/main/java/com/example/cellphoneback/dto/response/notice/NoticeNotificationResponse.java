package com.example.cellphoneback.dto.response.notice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "공지 알림 응답 DTO")
public class NoticeNotificationResponse {

    @Schema(description = "알림 ID", example = "101")
    private int id;

    @Schema(description = "알림 메시지", example = "새로운 공지사항이 등록되었습니다.")
    private String message;

    @Schema(description = "관련 링크", example = "/api/notice/123")
    private String link;

    @Schema(description = "읽음 여부", example = "false")
    private Boolean isRead;

    @Schema(description = "알림 생성일", example = "2026-02-06T09:00:00")
    private LocalDateTime createdAt;

    public NoticeNotificationResponse(int id, String message, String link, Boolean isRead, LocalDateTime createdAt) {
        this.id = id;
        this.message = message;
        this.link = link;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }
}