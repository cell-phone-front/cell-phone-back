package com.example.cellphoneback.dto.response.operation.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "Task 일괄 등록/수정/삭제 응답 DTO")
public class TaskBulkUpsertResponse {

    @Schema(description = "생성된 Task 개수", example = "5")
    private int createTask;

    @Schema(description = "삭제된 Task 개수", example = "2")
    private int deleteTask;

    @Schema(description = "수정된 Task 개수", example = "3")
    private int updateTask;
}