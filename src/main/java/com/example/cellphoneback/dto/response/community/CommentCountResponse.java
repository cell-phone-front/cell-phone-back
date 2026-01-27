package com.example.cellphoneback.dto.response.community;


import com.example.cellphoneback.entity.community.Community;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentCountResponse {
    Integer commentCount;

    public static CommentCountResponse fromEntity(Community community){
        return CommentCountResponse.builder()
                .commentCount(community.getCommentCount())
                .build();
    }
}
