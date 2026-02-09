package com.example.cellphoneback.dto.response.notice;

import com.example.cellphoneback.entity.notice.Notice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "공지사항 삭제 응답 DTO")
public class DeleteNoticeResponse {

    @Schema(description = "삭제 완료 메시지", example = "공지사항이 삭제되었습니다.")
    String message;

    public static DeleteNoticeResponse fromEntity(){
        return DeleteNoticeResponse.builder()
                .message("공지사항이 삭제되었습니다.")
                .build();
    }
}
