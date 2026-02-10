package com.example.cellphoneback.dto.response.notice;

import com.example.cellphoneback.entity.notice.Notice;
import com.example.cellphoneback.entity.notice.NoticeAttachment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Schema(description = "공지사항 단건 조회 응답 DTO")
public class SearchNoticeByIdResponse {

    @Schema(description = "공지사항 ID", example = "123")
    private int id;

    @Schema(description = "작성자 멤버 이름", example = "홍길동")
    private String memberName;

    @Schema(description = "공지사항 제목", example = "공지사항 제목 예시")
    private String title;

    @Schema(description = "공지사항 내용", example = "공지사항 내용 예시입니다.")
    private String content;

    @Schema(description = "작성일", example = "2026-02-06T09:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "공지사항 고정 여부", example = "true")
    private boolean pinned;

    @Schema(description = "조회수", example = "45")
    private int viewCount;

    @Schema(description = "첨부파일 리스트")
    private List<NoticeAttachmentResponse> attachments;

    public static SearchNoticeByIdResponse fromEntity(
            Notice notice,
            List<NoticeAttachment> attachments // null 가능
    ) {
        return SearchNoticeByIdResponse.builder()
                .id(notice.getId())
                .memberName(notice.getMember().getName())
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