package com.example.cellphoneback.dto.response.community.comment;

import com.example.cellphoneback.entity.community.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "커뮤니티 댓글 조회 응답 DTO")
public class SearchAllCommentResponse {

    @Schema(description = "댓글 ID", example = "1")
    private Integer id;

    @Schema(description = "댓글이 속한 커뮤니티 ID", example = "10")
    private Integer communityId;

    @Schema(description = "작성자 회원 ID", example = "user123")
    private String memberId;

    @Schema(description = "댓글 내용", example = "좋은 글이에요!")
    private String content;

    public static SearchAllCommentResponse fromEntity(Comment comment){
        return SearchAllCommentResponse.builder()
                .id(comment.getId())
                .communityId(comment.getCommunity().getId())
                .memberId(comment.getMember().getId())
                .content(comment.getContent())
                .build();
    }
}