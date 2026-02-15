package com.example.cellphoneback.dto.response.community.comment;

import com.example.cellphoneback.entity.community.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "커뮤니티 댓글 수정 응답 DTO")
public class EditCommentResponse {

    @Schema(description = "댓글이 속한 커뮤니티 ID", example = "10")
    private Integer communityId;

    @Schema(description = "익명 작성자 번호", example = "익명1")
    private String anonymous;

    @Schema(description = "댓글 내용", example = "수정된 댓글 내용입니다.")
    private String content;

    public static EditCommentResponse fromEntity(Comment comment, String anonymous) {
        return EditCommentResponse.builder()
                .communityId(comment.getCommunity().getId())
                .anonymous(anonymous)
                .content(comment.getContent())
                .build();
    }
}