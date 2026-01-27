package com.example.cellphoneback.dto.response.community.comment;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeleteCommentResponse {
    String message;

    public static DeleteCommentResponse fromEntity(){
        return DeleteCommentResponse.builder()
                .message("해당 글이 삭제되었습니다.")
                .build();
    }

}
