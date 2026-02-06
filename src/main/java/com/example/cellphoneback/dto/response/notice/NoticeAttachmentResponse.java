package com.example.cellphoneback.dto.response.notice;

import com.example.cellphoneback.entity.notice.NoticeAttachment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "공지사항 첨부파일 응답 DTO")
public class NoticeAttachmentResponse {

    @Schema(description = "첨부파일 ID", example = "file-12345")
    private String id;

    @Schema(description = "첨부파일 URL", example = "http://localhost:8080/files/file1.xlsx")
    private String fileUrl;

    @Schema(description = "첨부파일 크기 (byte)", example = "102400")
    private Long fileSize;

    @Schema(description = "첨부파일 타입", example = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    private String fileType;

    public static NoticeAttachmentResponse fromEntity(NoticeAttachment attachment) {
        return NoticeAttachmentResponse.builder()
                .id(attachment.getId())
                .fileUrl(attachment.getFileUrl())
                .fileSize(attachment.getFileSize())
                .fileType(attachment.getFileType())
                .build();
    }
}