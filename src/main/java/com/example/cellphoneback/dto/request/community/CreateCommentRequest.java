package com.example.cellphoneback.dto.request.community;

import com.example.cellphoneback.entity.community.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCommentRequest {
    private String content;


    public Comment toEntity() {
        return Comment.builder()
                .content(this.content)
                .build();
    }
}