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
@Schema(description = "공지사항 생성 요청 DTO")
public class CreateNoticeRequest {

    @Schema(description = "공지사항 제목", example = "신규 기능 업데이트 안내")
    String title;

    @Schema(description = "공지사항 내용", example = "이번 주에 새로운 기능이 추가되었습니다.")
    String content;

    @Schema(description = "상단 고정 여부", example = "true")
    Boolean pinned;

    public Notice toEntity() {
        return Notice.builder()
                .title(this.title)
                .content(this.content)
                .pinned(this.pinned)
                .build();
    }
}