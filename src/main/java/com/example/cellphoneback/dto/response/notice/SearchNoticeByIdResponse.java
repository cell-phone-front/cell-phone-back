package com.example.cellphoneback.dto.response.notice;

import com.example.cellphoneback.entity.notice.Notice;
import com.example.cellphoneback.entity.notice.NoticeAttachment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

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
    List<NoticeAttachmentResponse> attachments;

    public static SearchNoticeByIdResponse fromEntity(
            Notice notice,
            List<NoticeAttachment> attachments // null 가능
    ) {
        return SearchNoticeByIdResponse.builder()
                .id(notice.getId())
                .memberId(notice.getMember().getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt())
                .pinned(Boolean.TRUE.equals(notice.getPinned())) // null-safe
                .viewCount(notice.getViewCount())
                .attachments(
                        attachments == null ? List.of() :
                                attachments.stream()
                                        .map(NoticeAttachmentResponse::fromEntity)
                                        .toList()
                )
                .build();
    }
}