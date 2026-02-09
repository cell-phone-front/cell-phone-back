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
@Schema(description = "커뮤니티 게시글 수정 요청 DTO")
public class EditCommunityRequest {

    @Schema(description = "게시글 제목", example = "제목 수정")
    private String title;

    @Schema(description = "게시글 내용", example = "내용이 수정되었습니다.")
    private String content;

    public Community toEntity() {
        return Community.builder()
                .title(this.title)
                .content(this.content)
                .build();
    }
}