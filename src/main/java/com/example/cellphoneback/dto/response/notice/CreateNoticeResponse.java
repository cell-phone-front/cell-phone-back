package com.example.cellphoneback.dto.response.notice;

import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.entity.notice.Notice;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CreateNoticeResponse {

    String memberId;
    String title;
    String description;
    LocalDateTime createdAt;

    public static CreateNoticeResponse fromEntity(Notice notice){
        return CreateNoticeResponse.builder()
                .memberId(notice.getMember().getId())
                .title(notice.getTitle())
                .description(notice.getDescription())
                .createdAt(notice.getCreatedAt())
                .build();
    }
}
