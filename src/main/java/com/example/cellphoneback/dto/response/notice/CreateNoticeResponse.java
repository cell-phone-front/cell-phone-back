package com.example.cellphoneback.dto.response.notice;

import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.entity.notice.Notice;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CreateNoticeResponse {

    int id;
    String memberId;
    String title;
    String content;
    LocalDateTime createdAt;
    boolean pinned;
    int viewCount;

    public static CreateNoticeResponse fromEntity(Notice notice){
        return CreateNoticeResponse.builder()
                .id(notice.getId())
                .memberId(notice.getMember().getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt())
                .pinned(notice.isPinned())
                .viewCount(notice.getViewCount())
                .build();
    }
}
