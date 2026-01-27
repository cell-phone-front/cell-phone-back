package com.example.cellphoneback.dto.response.community;

import com.example.cellphoneback.entity.community.Community;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SearchCommunityByIdResponse {
        Integer id;
        String memberId;
        String title;
        String description;
        LocalDateTime createdAt;

        public static SearchCommunityByIdResponse fromEntity(Community community){
            return SearchCommunityByIdResponse.builder()
                    .id(community.getId())
                    .memberId(community.getMember().getId())
                    .title(community.getTitle())
                    .description(community.getDescription())
                    .createdAt(community.getCreatedAt())
                    .build();
        }
    }

