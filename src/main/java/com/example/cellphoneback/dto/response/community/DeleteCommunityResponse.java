package com.example.cellphoneback.dto.response.community;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeleteCommunityResponse {
    String message;

    public static DeleteCommunityResponse fromEntity(){
        return DeleteCommunityResponse.builder()
                .message("해당 글이 삭제되었습니다.")
                .build();
    }

}
