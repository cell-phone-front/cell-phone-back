package com.example.cellphoneback.dto.response.community;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "모든 커뮤니티 조회 응답 DTO")
public class SearchAllCommunityResponse {

    @Schema(description = "총 커뮤니티 개수", example = "25")
    private long totalCount;

    @Schema(description = "커뮤니티 리스트")
    private List<SearchCommunityByIdResponse> communityList;
}