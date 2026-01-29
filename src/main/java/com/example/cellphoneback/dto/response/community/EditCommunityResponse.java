package com.example.cellphoneback.dto.response.community;

import com.example.cellphoneback.entity.community.Community;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EditCommunityResponse {
    String title;
    String content;

    public static EditCommunityResponse fromEntity(Community community) {
        return EditCommunityResponse.builder()
                .title(community.getTitle())
                .content(community.getContent())
                .build();
    }
}
