package com.example.cellphoneback.dto.response.operation.operation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "Operation 일괄 등록/수정/삭제 응답 DTO")
public class OperationBulkUpsertResponse {

    @Schema(description = "생성된 Operation 개수", example = "5")
    private int createOperation;

    @Schema(description = "삭제된 Operation 개수", example = "2")
    private int deleteOperation;

    @Schema(description = "수정된 Operation 개수", example = "3")
    private int updateOperation;
}