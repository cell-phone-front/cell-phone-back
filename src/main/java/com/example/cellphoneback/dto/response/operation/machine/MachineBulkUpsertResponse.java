package com.example.cellphoneback.dto.response.operation.machine;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "Machine 일괄 등록/수정/삭제 응답 DTO")
public class MachineBulkUpsertResponse {

    @Schema(description = "생성된 Machine 개수", example = "5")
    private int createMachine;

    @Schema(description = "삭제된 Machine 개수", example = "2")
    private int deleteMachine;

    @Schema(description = "수정된 Machine 개수", example = "3")
    private int updateMachine;
}