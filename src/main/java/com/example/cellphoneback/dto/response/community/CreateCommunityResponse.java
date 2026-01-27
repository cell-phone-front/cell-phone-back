package com.example.cellphoneback.dto.response.community;

import com.example.cellphoneback.entity.community.Community;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CreateCommunityResponse {

    String title;
    String description;
    LocalDateTime createdAt;

    public static CreateCommunityResponse fromEntity(Community community){
        return CreateCommunityResponse.builder()
                .title(community.getTitle())
                .description(community.getDescription())
                .createdAt(community.getCreatedAt())
                .build();
    }
}
