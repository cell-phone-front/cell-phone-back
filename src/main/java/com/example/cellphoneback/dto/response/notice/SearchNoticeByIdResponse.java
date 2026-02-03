package com.example.cellphoneback.dto.response.notice;

import com.example.cellphoneback.entity.notice.Notice;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SearchNoticeByIdResponse {

    int id;
    String memberId;
    String title;
    String content;
    LocalDateTime createdAt;
    boolean pinned;
    int viewCount;

    public static SearchNoticeByIdResponse fromEntity(Notice notice){
        return SearchNoticeByIdResponse.builder()
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt())
                .pinned(notice.isPinned())
                .build();
    }
}
