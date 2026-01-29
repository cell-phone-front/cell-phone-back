package com.example.cellphoneback.dto.request.community;

import com.example.cellphoneback.entity.community.Community;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditCommunityRequest {
    private String title;
    private String content;


    public Community toEntity() {
        return Community.builder()
                .title(this.title)
                .content(this.content)
                .build();
    }
}