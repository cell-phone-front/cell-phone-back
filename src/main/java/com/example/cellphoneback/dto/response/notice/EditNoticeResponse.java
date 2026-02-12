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
@Schema(description = "공지사항 수정 응답 DTO")
public class EditNoticeResponse {

    @Schema(description = "공지사항 ID", example = "12")
    Integer id;

    @Schema(description = "공지사항 제목", example = "공지사항 제목 예시")
    String title;

    @Schema(description = "공지사항 내용", example = "공지사항 내용 예시")
    String content;

    @Schema(description = "공지사항 생성일", example = "2026-02-06T09:00:00")
    LocalDateTime createdAt;

    @Schema(description = "첨부파일 리스트")
    List<NoticeAttachmentResponse> noticeAttachmentList;

    public static EditNoticeResponse fromEntity(Notice notice, List<NoticeAttachment> noticeAttachments){
        return EditNoticeResponse.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt())
                // null 이면 빈 List를 넣고 null이 아니면 NoticeAttachmentFromEntity -> NoticeAttachmentResponse로 변환해서 List로 만듬
                .noticeAttachmentList(
                        noticeAttachments == null ? List.of() :
                                noticeAttachments.stream()
                                        .map(e -> NoticeAttachmentResponse.fromEntity(e)).toList()

                )
                .build();
    }
}