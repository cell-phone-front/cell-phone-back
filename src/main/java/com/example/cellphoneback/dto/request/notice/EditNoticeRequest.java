package com.example.cellphoneback.dto.request.notice;

import com.example.cellphoneback.entity.notice.Notice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public Notice toEntity(Notice notice) {
        return Notice.builder()
                .title(this.title)
                .content(this.content)
                .build();
    }
}