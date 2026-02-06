package com.example.cellphoneback.dto.response.notice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "공지사항 고정/해제 응답 DTO")
public class PinNoticeResponse {

    @Schema(description = "공지사항 ID", example = "123")
    private int noticeId;

    @Schema(description = "공지사항 고정 여부", example = "true")
    private boolean pinned;
}