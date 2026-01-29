package com.example.cellphoneback.dto.request.notice;

import com.example.cellphoneback.entity.notice.Notice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateNoticeRequest {
    String title;
    String content;

    public Notice toEntity() {
        return Notice.builder()
                .title(this.title)
                .content(this.content)
                .build();
    }
}
