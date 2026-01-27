package com.example.cellphoneback.dto.response.notice;

import com.example.cellphoneback.entity.notice.Notice;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeleteNoticeResponse {

    String message;

    public static DeleteNoticeResponse fromEntity(){
        return DeleteNoticeResponse.builder()
                .message("공지사항이 삭제되었습니다.")
                .build();
    }
}
