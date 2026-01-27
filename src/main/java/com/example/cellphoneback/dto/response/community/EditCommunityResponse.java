package com.example.cellphoneback.dto.response.community;

import com.example.cellphoneback.entity.community.Community;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EditCommunityResponse {
    String title;
    String description;

    public static EditCommunityResponse fromEntity(Community community) {
        return EditCommunityResponse.builder()
                .title(community.getTitle())
                .description(community.getDescription())
                .build();
    }
}
