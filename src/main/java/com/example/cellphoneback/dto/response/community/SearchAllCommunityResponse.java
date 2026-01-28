package com.example.cellphoneback.dto.response.community;

import com.example.cellphoneback.entity.community.Community;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchAllCommunityResponse {
    Integer id;
    String memberId;
    String title;
    String description;
    java.time.LocalDateTime createdAt;
    int commentCount;

    public static SearchAllCommunityResponse fromEntity(Community community){
        return SearchAllCommunityResponse.builder()
                .id(community.getId())
                .memberId(community.getMember().getId())
                .title(community.getTitle())
                .description(community.getDescription())
                .createdAt(community.getCreatedAt())
                .commentCount(community.getCommentCount())
                .build();
    }
}

