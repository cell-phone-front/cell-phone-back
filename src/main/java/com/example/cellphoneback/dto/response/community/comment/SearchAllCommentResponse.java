package com.example.cellphoneback.dto.response.community.comment;

import com.example.cellphoneback.entity.community.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "커뮤니티 댓글 조회 응답 DTO")
public class SearchAllCommentResponse {

    @Schema(description = "댓글 ID", example = "1")
    private Integer id;

    @Schema(description = "익명 작성자 번호", example = "익명1")
    private String anonymous;

    @Schema(description = "댓글 내용", example = "좋은 글이에요!")
    private String content;

    @Schema(description = "댓글 작성 시간", example = "2020-01-26 09:00")
    private LocalDateTime createdAt;


    public static SearchAllCommentResponse fromEntity(Comment comment, String anonymous){
        return SearchAllCommentResponse.builder()
                .id(comment.getId())
                .anonymous(anonymous)
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}