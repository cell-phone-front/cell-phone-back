package com.example.cellphoneback.dto.response.community;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SearchAllCommunityResponse {
    private long totalCount;
    private List<SearchCommunityByIdResponse> communityList;
}

