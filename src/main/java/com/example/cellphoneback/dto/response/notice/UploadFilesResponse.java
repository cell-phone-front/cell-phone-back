package com.example.cellphoneback.dto.response.notice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@Schema(description = "파일 업로드 응답 DTO")
public class UploadFilesResponse {

    @Schema(description = "업로드된 파일 URL 리스트", example = "[\"http://localhost:8080/files/file1.xlsx\", \"http://localhost:8080/files/file2.pdf\"]")
    private List<String> fileUrls;
}