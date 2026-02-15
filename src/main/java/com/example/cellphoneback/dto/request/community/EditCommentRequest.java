package com.example.cellphoneback.dto.request.community;

import com.example.cellphoneback.entity.community.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "댓글 수정 요청 DTO")
public class EditCommentRequest {

    @Schema(description = "댓글 내용", example = "댓글 내용이 수정되었습니다.")
    private String content;

}