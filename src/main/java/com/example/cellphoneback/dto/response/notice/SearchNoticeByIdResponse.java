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

    // 상세 조회용 (첨부파일 포함)
    public static SearchNoticeByIdResponse fromEntity(
            Notice notice,
            List<NoticeAttachment> attachments
    ) {
        return SearchNoticeByIdResponse.builder()
                .id(notice.getId())
                .memberId(notice.getMember().getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt())
                .pinned(notice.isPinned())
                .viewCount(notice.getViewCount())
                .attachments(
                        attachments.stream()
                                .map(NoticeAttachmentResponse::fromEntity)
                                .toList()
                )
                .build();
    }

    // 목록 조회용 (첨부파일 없음)
    public static SearchNoticeByIdResponse fromEntity(Notice notice) {
        return SearchNoticeByIdResponse.builder()
                .id(notice.getId())
                .memberId(notice.getMember().getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt())
                .pinned(notice.isPinned())
                .viewCount(notice.getViewCount())
                .attachments(List.of())
                .build();
    }
}