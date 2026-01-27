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
public class EditNoticeRequest {
    String title;
    String description;

    public Notice toEntity(Notice notice) {
        return Notice.builder()
                .title(this.title)
                .description(this.description)
                .build();
    }
}
