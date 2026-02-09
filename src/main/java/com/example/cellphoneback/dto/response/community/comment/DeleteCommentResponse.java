package com.example.cellphoneback.dto.response.community.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "커뮤니티 댓글 삭제 응답 DTO")
public class DeleteCommentResponse {

    @Schema(description = "삭제 결과 메시지", example = "해당 글이 삭제되었습니다.")
    private String message;

    public static DeleteCommentResponse fromEntity(){
        return DeleteCommentResponse.builder()
                .message("해당 글이 삭제되었습니다.")
                .build();
    }
}