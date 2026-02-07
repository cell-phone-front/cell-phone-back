package com.example.cellphoneback.dto.request.community;

import com.example.cellphoneback.entity.community.Community;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "커뮤니티 게시글 생성 요청 DTO")
public class CreateCommunityRequest {

    @Schema(description = "게시글 제목", example = "자유 게시글 제목")
    private String title;

    @Schema(description = "게시글 내용", example = "게시글 내용입니다.")
    private String content;

    public Community toEntity() {
        return Community.builder()
                .title(this.title)
                .content(this.content)
                .build();
    }
}