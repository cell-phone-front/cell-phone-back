package com.example.cellphoneback.dto.response.notice;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PinNoticeResponse {
    private int noticeId;
    private boolean pinned;
}

