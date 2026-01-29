package com.example.cellphoneback.dto.response.notice;

import com.example.cellphoneback.entity.notice.Notice;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class EditNoticeResponse {

    String title;
    String content;
    LocalDateTime createdAt;

    public static EditNoticeResponse fromEntity(Notice notice){
        return EditNoticeResponse.builder()
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt())
                .build();
    }
}
