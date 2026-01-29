package com.example.cellphoneback.dto.response.notice;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SearchAllNoticeResponse {

    private long totalNoticeCount;
    private List<SearchNoticeByIdResponse> noticeList;
}
