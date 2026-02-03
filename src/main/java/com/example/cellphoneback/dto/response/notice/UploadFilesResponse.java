package com.example.cellphoneback.dto.response.notice;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UploadFilesResponse {

    private List<String> fileUrls;
}