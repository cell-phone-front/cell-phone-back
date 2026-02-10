package com.example.cellphoneback.dto.response.community.comment;

import com.example.cellphoneback.entity.community.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "커뮤니티 댓글 생성 응답 DTO")
public class CreateCommentResponse {

    @Schema(description = "댓글이 속한 커뮤니티 ID", example = "10")
    private Integer communityId;

    @Schema(description = "댓글 작성자 회원 ID", example = "user123")
    private String memberId;

    @Schema(description = "댓글 내용", example = "댓글 내용입니다.")
    private String content;

    @Schema(description = "댓글 작성 시간", example = "2020-01-26 09:00")
    private LocalDateTime createdAt;

    public static CreateCommentResponse fromEntity(Comment comment){
        return CreateCommentResponse.builder()
                .communityId(comment.getCommunity().getId())
                .memberId(comment.getMember().getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}