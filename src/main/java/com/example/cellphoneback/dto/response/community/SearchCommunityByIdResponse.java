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
        String content;
        LocalDateTime createdAt;
        int commentCount;
        int viewCount;

        public static SearchCommunityByIdResponse fromEntity(Community community){
            return SearchCommunityByIdResponse.builder()
                    .id(community.getId())
                    .memberId(community.getMember().getId())
                    .title(community.getTitle())
                    .content(community.getContent())
                    .createdAt(community.getCreatedAt())
                    .commentCount(community.getCommentCount())
                    .viewCount(community.getViewCount())
                    .build();
        }
    }

