package com.example.cellphoneback.dto.request.notice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "공지사항 수정 요청 DTO")
public class EditNoticeRequest {

    @Schema(description = "공지사항 제목", example = "공지사항 제목 수정")
    String title;

    @Schema(description = "공지사항 내용", example = "공지사항 내용이 수정되었습니다.")
    String content;

    @Schema(description = "삭제할 첨부파일 ID 목록", example = "[\"a1b2c3\", \"d4e5f6\"]")
    List<String> deleteAttachmentIds;

}