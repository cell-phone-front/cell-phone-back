package com.example.cellphoneback.dto.response.notice;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NoticeNotificationResponse {

    private Long id;
    private String message;
    private String link;
    private Boolean isRead;
    private LocalDateTime createdAt;

    public NoticeNotificationResponse(Long id, String message, String link, Boolean isRead, LocalDateTime createdAt) {
        this.id = id;
        this.message = message;
        this.link = link;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }
}