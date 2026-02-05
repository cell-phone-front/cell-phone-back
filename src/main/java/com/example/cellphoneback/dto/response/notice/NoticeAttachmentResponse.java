package com.example.cellphoneback.dto.response.notice;

import com.example.cellphoneback.entity.notice.NoticeAttachment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NoticeAttachmentResponse {

    private String id;
    private String fileUrl;
    private Long fileSize;
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