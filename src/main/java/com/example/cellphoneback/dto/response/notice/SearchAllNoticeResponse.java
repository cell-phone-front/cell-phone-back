package com.example.cellphoneback.dto.response.notice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "전체 공지사항 조회 응답 DTO")
public class SearchAllNoticeResponse {

    @Schema(description = "전체 공지사항 수", example = "120")
    long totalNoticeCount;

    @Schema(description = "공지사항 리스트")
    List<NoticeListResponse> noticeList;
}