package com.example.cellphoneback.dto.response.notice;

import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.entity.notice.Notice;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SearchAllNoticeResponse {

    String title;
    String name;
    String description;
    LocalDateTime createdAt;

    public static SearchAllNoticeResponse fromEntity(Member member, Notice notice){
        return SearchAllNoticeResponse.builder()
                .title(notice.getTitle())
                .name(member.getName())
                .description(notice.getDescription())
                .createdAt(notice.getCreatedAt())
                .build();
    }
}
