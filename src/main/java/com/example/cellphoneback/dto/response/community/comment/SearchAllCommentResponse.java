package com.example.cellphoneback.dto.response.community.comment;

import com.example.cellphoneback.entity.community.Comment;
import com.example.cellphoneback.entity.community.Community;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchAllCommentResponse {
    Integer id;
    Integer communityId;
    String memberId;
    String content;


    public static SearchAllCommentResponse fromEntity(Comment comment){
        return SearchAllCommentResponse.builder()
                .id(comment.getId())
                .communityId(comment.getCommunity().getId())
                .memberId(comment.getMember().getId())
                .content(comment.getContent())
                .build();
    }
}

