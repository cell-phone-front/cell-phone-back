package com.example.cellphoneback.dto.response.notice;

import com.example.cellphoneback.entity.notice.Notice;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SearchAllNoticeResponse {

    String title;
    String description;
    LocalDateTime createdAt;

    public static SearchAllNoticeResponse fromEntity(Notice notice){
        return SearchAllNoticeResponse.builder()
                .title(notice.getTitle())
                .description(notice.getDescription())
                .createdAt(notice.getCreatedAt())
                .build();
    }
}
