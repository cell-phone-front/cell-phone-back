package com.example.cellphoneback.dto.response.community.comment;

import com.example.cellphoneback.entity.community.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateCommentResponse {

    Integer communityId;
    String memberId;
    String content;

    public static CreateCommentResponse fromEntity(Comment comment){
        return CreateCommentResponse.builder()
                .communityId(comment.getCommunity().getId())
                .memberId(comment.getMember().getId())
                .content(comment.getContent())
                .build();
    }
}
